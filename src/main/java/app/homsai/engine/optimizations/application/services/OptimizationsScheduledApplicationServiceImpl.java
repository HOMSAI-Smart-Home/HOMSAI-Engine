package app.homsai.engine.optimizations.application.services;

import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.application.services.EntitiesScheduledApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.homeassistant.application.services.HomeAssistantCommandsApplicationService;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.optimizations.application.http.converters.OptimizationsMapper;
import app.homsai.engine.optimizations.application.http.dtos.HvacDeviceDto;
import app.homsai.engine.optimizations.domain.models.HvacDevice;
import app.homsai.engine.optimizations.gateways.HomsaiAIServiceGateway;
import app.homsai.engine.optimizations.gateways.dtos.HvacDevicesOptimizationPVResponseDto;
import app.homsai.engine.optimizations.gateways.dtos.HvacOptimizationPVRequestDto;
import app.homsai.engine.optimizations.infrastructure.cache.HVACRunningDevicesCacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static app.homsai.engine.common.domain.utils.Consts.*;

@Service
public class OptimizationsScheduledApplicationServiceImpl implements OptimizationsScheduledApplicationService {

    @Autowired
    HVACRunningDevicesCacheRepository hvacRunningDevicesCacheRepository;

    @Autowired
    HomsaiAIServiceGateway homsaiAIServiceGateway;

    @Autowired
    HomeAssistantQueriesApplicationService homeAssistantQueriesApplicationService;

    @Autowired
    HomeAssistantCommandsApplicationService homeAssistantCommandsApplicationService;

    @Autowired
    EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    @Autowired
    EntitiesCommandsApplicationService entitiesCommandsApplicationService;

    @Autowired
    OptimizationsMapper optimizationsMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Scheduled(fixedRate = Consts.HVAC_PV_OPTIMIZATION_UPDATE_TIMEDELTA_MINUTES*60*1000)
    public void updateHvacDeviceOptimizationCache() {
        hvacRunningDevicesCacheRepository.updateHvacDevicesCache();
    }

    @Scheduled(fixedRate = Consts.HVAC_PV_OPTIMIZATION_REQUEST_TIMEDELTA_MINUTES*60*1000)
    public void updateHvacDeviceOptimizationStatus(){
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        if(!homeInfo.getPvOptimizationsEnabled())
            return;
        if(homeInfo.getGeneralPowerMeterId() == null || homeInfo.getHvacPowerMeterId() == null || homeInfo.getPvProductionSensorId() == null){
            homeInfo.setPvOptimizationsEnabled(false);
            entitiesCommandsApplicationService.saveHomeInfo(homeInfo);
            return;
        }
        HashMap<String, HvacDevice> hvacDeviceHashMap = hvacRunningDevicesCacheRepository.getHvacDevicesCache();
        List<HvacDeviceDto> hvacDevices = optimizationsMapper.convertToDto(hvacDeviceHashMap).stream()
                .filter(HvacDeviceDto::getEnabled)
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
        hvacOptimizationPVRequestDto.setMinimumIdleTimeMinutes(HVAC_PV_OPTIMIZATION_MINIMUM_IDLE_MINUTES);
        hvacOptimizationPVRequestDto.setMinimumExecutionTimeMinutes(HVAC_PV_OPTIMIZATION_MINIMUM_EXECUTION_MINUTES);
        HvacDevicesOptimizationPVResponseDto hvacDevicesOptimizationPVResponseDto = homsaiAIServiceGateway.getHvacDevicesOptimizationPV(hvacOptimizationPVRequestDto);
        if(hvacDevicesOptimizationPVResponseDto.getDevicesToTurnOn() != null) {
            if(hvacDevicesOptimizationPVResponseDto.getDevicesToTurnOn().size() > 0) {
                logger.info("[HVAC Optimizer] Home consumption:" + globalConsumptionPower + ",PV Production:" + solarProductionPower + ", Storage power:" + storagePower);
                logger.info("[HVAC Optimizer] HVAC devices to turn on: " + hvacDevicesOptimizationPVResponseDto.getDevicesToTurnOn().size());
            }
            for(String hvacDeviceEntityId : hvacDevicesOptimizationPVResponseDto.getDevicesToTurnOn()){
                Integer setTemp = hvacDeviceHashMap.get(hvacDeviceEntityId).getSetTemperature().intValue() - HVAC_THRESHOLD_TEMPERATURE.intValue();
                homeAssistantCommandsApplicationService.sendHomeAssistantClimateTemperature(hvacDeviceEntityId, setTemp.doubleValue());

                homeAssistantCommandsApplicationService.sendHomeAssistantClimateHVACMode(hvacDeviceEntityId, HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION);
                hvacDeviceHashMap.get(hvacDeviceEntityId).setActive(true);
                hvacDeviceHashMap.get(hvacDeviceEntityId).setStartTime(Instant.now());
                logger.info("[HVAC Optimizer] HVAC device turned on: "+hvacDeviceEntityId);
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
                hvacDeviceHashMap.get(hvacDeviceEntityId).setEndTime(Instant.now());
                logger.info("[HVAC Optimizer] HVAC device turned off: "+hvacDeviceEntityId+", current consumption: "+oldConsumption);
            }
        } else
            logger.info("[HVAC Optimizer] HVAC devices to turn off: 0");
    }
}
