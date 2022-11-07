package app.homsai.engine.pvoptimizer.domain.services;

import app.homsai.engine.common.domain.utils.constants.Consts;
import app.homsai.engine.common.domain.utils.constants.ConstsUtils;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.homeassistant.application.services.HomeAssistantCommandsApplicationService;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.pvoptimizer.application.http.converters.PVOptimizerMapper;
import app.homsai.engine.pvoptimizer.application.http.dtos.OptimizerHVACDeviceDto;
import app.homsai.engine.pvoptimizer.domain.models.HVACEquipment;
import app.homsai.engine.pvoptimizer.domain.models.OptimizerHVACDevice;
import app.homsai.engine.pvoptimizer.gateways.PVOptimizerHomsaiAIServiceGateway;
import app.homsai.engine.pvoptimizer.gateways.dtos.HvacDevicesOptimizationPVResponseDto;
import app.homsai.engine.pvoptimizer.gateways.dtos.HvacOptimizationPVRequestDto;
import app.homsai.engine.pvoptimizer.domain.services.cache.PVOptimizerCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static app.homsai.engine.common.domain.utils.constants.Consts.*;

@Service
public class PVOptimizerEngineServiceImpl implements PVOptimizerEngineService {

    @Autowired
    PVOptimizerCacheService pvoptimizerCacheService;

    @Autowired
    PVOptimizerHomsaiAIServiceGateway PVOptimizerHomsaiAIServiceGateway;

    @Autowired
    HomeAssistantQueriesApplicationService homeAssistantQueriesApplicationService;

    @Autowired
    HomeAssistantCommandsApplicationService homeAssistantCommandsApplicationService;

    @Autowired
    EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    @Autowired
    EntitiesCommandsApplicationService entitiesCommandsApplicationService;

    @Autowired
    PVOptimizerCommandsService pvOptimizerCommandsService;

    @Autowired
    PVOptimizerQueriesService pvOptimizerQueriesService;

    @Autowired
    PVOptimizerMapper pvoptimizerMapper;

    @Autowired
    ConstsUtils constsUtils;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public void updateHvacDeviceOptimizationCache() {
        pvoptimizerCacheService.updateHvacDevicesCache();
    }

    @Override
    public void updateHvacDeviceOptimizationStatus(){
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        if(!homeInfo.getPvOptimizationsEnabled())
            return;
        if(homeInfo.getGeneralPowerMeterId() == null ||
                (homeInfo.getHvacPowerMeterId(homeInfo.getOptimizerMode()) == null) ||
                homeInfo.getPvProductionSensorId() == null) {
            homeInfo.setPvOptimizationsEnabled(false);
            entitiesCommandsApplicationService.saveHomeInfo(homeInfo);
            return;
        }
        HashMap<String, OptimizerHVACDevice> hvacDeviceHashMap = pvoptimizerCacheService.getHvacDevicesCache();
        List<OptimizerHVACDeviceDto> hvacDevices = pvoptimizerMapper.convertToDto(hvacDeviceHashMap).stream()
                .filter(OptimizerHVACDeviceDto::getEnabled)
                .filter(h -> !h.isManual())
                .collect(Collectors.toList());
        HomeAssistantEntityDto globalConsumptionPowerEntity = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(homeInfo.getGeneralPowerMeterId());
        HomeAssistantEntityDto solarProductionPowerEntity = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(homeInfo.getPvProductionSensorId());
        double solarProductionPower = HOME_ASSISTANT_WATT.equals(solarProductionPowerEntity.getAttributes().getUnitOfMeasurement())? Double.parseDouble(solarProductionPowerEntity.getState()) : Double.parseDouble(solarProductionPowerEntity.getState()) * 1000;
        double globalConsumptionPower = HOME_ASSISTANT_WATT.equals(globalConsumptionPowerEntity.getAttributes().getUnitOfMeasurement())? Double.parseDouble(globalConsumptionPowerEntity.getState()) : Double.parseDouble(globalConsumptionPowerEntity.getState()) * 1000;
        double storagePower = 0D;
        if(homeInfo.getPvStorageSensorId() != null) {
            HomeAssistantEntityDto storagePowerEntity = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(homeInfo.getPvStorageSensorId());
            storagePower = HOME_ASSISTANT_WATT.equals(storagePowerEntity.getAttributes().getUnitOfMeasurement()) ? Double.parseDouble(storagePowerEntity.getState()) : Double.parseDouble(storagePowerEntity.getState()) * 1000;
        }
        HvacOptimizationPVRequestDto hvacOptimizationPVRequestDto = new HvacOptimizationPVRequestDto();
        hvacOptimizationPVRequestDto.setHvacDevices(hvacDevices);
        hvacOptimizationPVRequestDto.setCycleTime(Consts.HVAC_PV_OPTIMIZATION_REQUEST_TIMEDELTA_MINUTES);
        hvacOptimizationPVRequestDto.setGeneralPowerMeterValue(globalConsumptionPower);
        hvacOptimizationPVRequestDto.setPhotovoltaicPowerMeterValue(solarProductionPower);
        hvacOptimizationPVRequestDto.setStoragePowerMeterValue(storagePower);

        Integer minimumIdleMinutes = constsUtils.getHvacPvOptimizationMinimumIdleMinutes();
        Integer minimumExecutionMinutes = constsUtils.getHvacPvOptimizationMinimumExecutionMinutes();
        HVACEquipment currentHVACEquipment = homeInfo.getOptimizerMode() == null ||
                homeInfo.getOptimizerMode() == Consts.HVAC_MODE_SUMMER_ID ?
                homeInfo.getCurrentSummerHVACEquipment() : homeInfo.getCurrentWinterHVACEquipment();
        if (currentHVACEquipment != null) {
            minimumIdleMinutes = currentHVACEquipment.getMinimumIdleMinutes();
            minimumExecutionMinutes = currentHVACEquipment.getMinimumExecutionMinutes();
        }

        hvacOptimizationPVRequestDto.setMinimumIdleTimeMinutes(minimumIdleMinutes);
        hvacOptimizationPVRequestDto.setMinimumExecutionTimeMinutes(minimumExecutionMinutes);

        HvacDevicesOptimizationPVResponseDto hvacDevicesOptimizationPVResponseDto = PVOptimizerHomsaiAIServiceGateway.getHvacDevicesOptimizationPV(hvacOptimizationPVRequestDto);
        if(hvacDevicesOptimizationPVResponseDto.getDevicesToTurnOn() != null) {
            if(hvacDevicesOptimizationPVResponseDto.getDevicesToTurnOn().size() > 0) {
                logger.info("[HVAC Optimizer] Home consumption:" + globalConsumptionPower + ",PV Production:" + solarProductionPower + ", Storage power:" + storagePower);
                logger.info("[HVAC Optimizer] HVAC devices to turn on: " + hvacDevicesOptimizationPVResponseDto.getDevicesToTurnOn().size());
            }
            for(String hvacDeviceEntityId : hvacDevicesOptimizationPVResponseDto.getDevicesToTurnOn()){
                Double setTemp =
                        homeInfo.getOptimizerMode() == Consts.HVAC_MODE_SUMMER_ID ?
                                hvacDeviceHashMap.get(hvacDeviceEntityId).getSetTemperature() - HVAC_THRESHOLD_TEMPERATURE:
                                hvacDeviceHashMap.get(hvacDeviceEntityId).getSetTemperature() + HVAC_THRESHOLD_TEMPERATURE;
                homeAssistantCommandsApplicationService.sendHomeAssistantClimateTemperature(hvacDeviceEntityId, setTemp);

                String hvacDeviceFunction = homeInfo.getOptimizerMode() == null ||
                        homeInfo.getOptimizerMode() == Consts.HVAC_MODE_SUMMER_ID ?
                        Consts.HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION :
                        Consts.HOME_ASSISTANT_HVAC_DEVICE_HEATING_FUNCTION;
                homeAssistantCommandsApplicationService.sendHomeAssistantClimateHVACMode(hvacDeviceEntityId, hvacDeviceFunction);
                hvacDeviceHashMap.get(hvacDeviceEntityId).setActive(true);
                hvacDeviceHashMap.get(hvacDeviceEntityId).setHvacMode(hvacDeviceFunction);
                hvacDeviceHashMap.get(hvacDeviceEntityId).setStartTime(Instant.now());
                logger.info("[HVAC Optimizer] HVAC device turned on: "+hvacDeviceEntityId);
                if(homeInfo.getHvacSwitchEntityId() != null)
                    homeAssistantCommandsApplicationService.sendHomeAssistantSwitchMode(homeInfo.getHvacSwitchEntityId(), true);
            }
        } else
            logger.info("[HVAC Optimizer] HVAC devices to turn on: 0");
        if(hvacDevicesOptimizationPVResponseDto.getDevicesToTurnOff() != null) {
            if(hvacDevicesOptimizationPVResponseDto.getDevicesToTurnOff().size() > 0) {
                logger.info("[HVAC Optimizer] Home consumption:" + globalConsumptionPower + ",PV Production:" + solarProductionPower + ", Storage power:" + storagePower);
                logger.info("[HVAC Optimizer] HVAC devices to turn off: " + hvacDevicesOptimizationPVResponseDto.getDevicesToTurnOff().size());
            }
            for(String hvacDeviceEntityId : hvacDevicesOptimizationPVResponseDto.getDevicesToTurnOff()){
                Double oldConsumption = hvacDeviceHashMap.get(hvacDeviceEntityId).getActualPowerConsumption();
                homeAssistantCommandsApplicationService.sendHomeAssistantClimateHVACMode(hvacDeviceEntityId, HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION);
                hvacDeviceHashMap.get(hvacDeviceEntityId).setActive(false);
                hvacDeviceHashMap.get(hvacDeviceEntityId).setHvacMode(HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION);
                hvacDeviceHashMap.get(hvacDeviceEntityId).setEndTime(Instant.now());
                logger.info("[HVAC Optimizer] HVAC device turned off: "+hvacDeviceEntityId+", current consumption: "+oldConsumption);
                if(homeInfo.getHvacSwitchEntityId() != null && pvOptimizerCommandsService.noClimateDeviceIsOn(pvOptimizerQueriesService.findAllHomsaiHvacDevices(Pageable.unpaged(), null).getContent()))
                    homeAssistantCommandsApplicationService.sendHomeAssistantSwitchMode(homeInfo.getHvacSwitchEntityId(), false);
            }
        } else
            logger.info("[HVAC Optimizer] HVAC devices to turn off: 0");
    }
}
