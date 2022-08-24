package app.homsai.engine.pvoptimizer.domain.services.cache;

import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.domain.services.cache.HomsaiEntityShowCacheService;
import app.homsai.engine.entities.application.http.dtos.AreaDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HVACDeviceDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntityShowDto;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.pvoptimizer.application.http.converters.PVOptimizerMapper;
import app.homsai.engine.pvoptimizer.application.services.PVOptimizerQueriesApplicationService;
import app.homsai.engine.pvoptimizer.domain.models.OptimizerHVACDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static app.homsai.engine.common.domain.utils.Consts.HOME_ASSISTANT_WATT;


@Service
public class PVOptimizerCacheServiceImpl implements PVOptimizerCacheService {

    @Autowired
    private EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    @Autowired
    private PVOptimizerQueriesApplicationService pvOptimizerQueriesApplicationService;

    @Autowired
    private HomeAssistantQueriesApplicationService homeAssistantQueriesApplicationService;

    @Autowired
    private HomsaiEntityShowCacheService homsaiEntityShowCacheService;

    @Autowired
    private HomsaiOptimizerHVACDeviceInitializationCacheService HomsaiOptimizerHVACDeviceInitializationCacheService;

    @Autowired
    PVOptimizerMapper pvoptimizerMapper;

    private static HashMap<String, OptimizerHVACDevice> hvacDevicesCache = null;


    @Override
    public HashMap<String, OptimizerHVACDevice> getHvacDevicesCache() {
        if(hvacDevicesCache == null || hvacDevicesCache.size() == 0)
            initHvacDevicesCache();
        return hvacDevicesCache;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetManualDevices() {
        for(OptimizerHVACDevice optimizerHVACDevice : getHvacDevicesCache().values()){
            optimizerHVACDevice.setManual(false);
        }
    }

    @Override
    public void initHvacDevicesCache() {
        hvacDevicesCache = new HashMap<>();
        List<HVACDeviceDto> hvacDeviceDtoList = pvOptimizerQueriesApplicationService.getAllHomsaiHvacDevices(Consts.HVAC_DEVICE_CONDITIONING);
        List<AreaDto> areaList = entitiesQueriesApplicationService.getAllAreas();
        Double homeSetTemperature = areaList.stream()
                .filter(area -> area.getUuid().equals(Consts.HOME_AREA_UUID))
                .map(AreaDto::getDesiredSummerTemperature)
                .findFirst().orElse(null);
        for(HVACDeviceDto hvacDeviceDto : hvacDeviceDtoList){
            OptimizerHVACDevice optimizerHVACDevice = new OptimizerHVACDevice();
            optimizerHVACDevice.setEntityId(hvacDeviceDto.getEntityId());
            optimizerHVACDevice.setAreaId(hvacDeviceDto.getArea().getName());
            Double currentTemperature = homsaiEntityShowCacheService.getHomsaiEntityShowDtoList().stream()
                    .filter(hD -> hD.getArea().equals(hvacDeviceDto.getArea().getName()))
                    .map(HomsaiEntityShowDto::getTemperatureD)
                    .map(Optional::ofNullable)
                    .findFirst().flatMap(Function.identity()).orElse(null);
            optimizerHVACDevice.setCurrentTemperature(currentTemperature);
            Double setTemperature = areaList.stream()
                    .filter(area -> area.getName().equals(hvacDeviceDto.getArea().getName()))
                    .map(AreaDto::getDesiredSummerTemperature)
                    .map(Optional::ofNullable)
                    .findFirst().flatMap(Function.identity())
                    .orElse(null);
            if(setTemperature == null) setTemperature=homeSetTemperature;
            optimizerHVACDevice.setSetTemperature(setTemperature);
            optimizerHVACDevice.setActive(false);
            optimizerHVACDevice.setStartTime(null);
            optimizerHVACDevice.setEndTime(Instant.EPOCH);
            optimizerHVACDevice.setPowerConsumption(hvacDeviceDto.getPowerConsumption());
            optimizerHVACDevice.setIntervals(pvoptimizerMapper.convertToDtoIntervals(hvacDeviceDto.getIntervals()));
            if(hvacDeviceDto.getEnabled() == null)
                optimizerHVACDevice.setEnabled(false);
            else
                optimizerHVACDevice.setEnabled(hvacDeviceDto.getEnabled());
            hvacDevicesCache.put(optimizerHVACDevice.getEntityId(), optimizerHVACDevice);
        }
    }

    @Override
    public void updateHvacDevicesCache(){
        List<HVACDeviceDto> hvacDeviceDtoList = pvOptimizerQueriesApplicationService.getAllHomsaiHvacDevices(Consts.HVAC_DEVICE_CONDITIONING);
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        if(homeInfo.getHvacPowerMeterId() == null)
            return;
        if(HomsaiOptimizerHVACDeviceInitializationCacheService.getHvacDeviceCacheDto().getInProgress())
            return;
        List<AreaDto> areaList = entitiesQueriesApplicationService.getAllAreas();
        Double homeSetTemperature = areaList.stream()
                .filter(area -> area.getUuid().equals(Consts.HOME_AREA_UUID))
                .map(AreaDto::getDesiredSummerTemperature)
                .findFirst().orElse(null);
        HomeAssistantEntityDto climatePowerEntityDto = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(homeInfo.getHvacPowerMeterId());
        double currentTotalClimateConsumption = HOME_ASSISTANT_WATT.equals(climatePowerEntityDto.getAttributes().getUnitOfMeasurement())? Double.parseDouble(climatePowerEntityDto.getState()) : Double.parseDouble(climatePowerEntityDto.getState()) * 1000;
        int active = 0;
        for(HVACDeviceDto hvacDeviceDto : hvacDeviceDtoList){
            OptimizerHVACDevice optimizerHVACDevice = getHvacDevicesCache().get(hvacDeviceDto.getEntityId());
            if(hvacDeviceDto.getEnabled() == null)
                optimizerHVACDevice.setEnabled(false);
            else
                optimizerHVACDevice.setEnabled(hvacDeviceDto.getEnabled());
            optimizerHVACDevice.setIntervals(pvoptimizerMapper.convertToDtoIntervals(hvacDeviceDto.getIntervals()));
            Double currentTemperature = homsaiEntityShowCacheService.getHomsaiEntityShowDtoList().stream()
                    .filter(hD -> hD.getArea().equals(hvacDeviceDto.getArea().getName()))
                    .map(HomsaiEntityShowDto::getTemperatureD)
                    .map(Optional::ofNullable)
                    .findFirst().flatMap(Function.identity()).orElse(null);
            optimizerHVACDevice.setCurrentTemperature(currentTemperature);
            Double setTemperature = areaList.stream()
                    .filter(area -> area.getName().equals(hvacDeviceDto.getArea().getName()))
                    .map(AreaDto::getDesiredSummerTemperature)
                    .map(Optional::ofNullable)
                    .findFirst().flatMap(Function.identity())
                    .orElse(null);
            if(setTemperature == null) setTemperature=homeSetTemperature;
            optimizerHVACDevice.setSetTemperature(setTemperature);
            HomeAssistantEntityDto hvacDeviceEntity = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(optimizerHVACDevice.getEntityId());
            if(!optimizerHVACDevice.getActive() && !Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION.equals(hvacDeviceEntity.getState())){
                optimizerHVACDevice.setActive(true);
                optimizerHVACDevice.setStartTime(Instant.now());
                optimizerHVACDevice.setManual(true);
            } else if(optimizerHVACDevice.getActive() && Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION.equals(hvacDeviceEntity.getState())){
                optimizerHVACDevice.setActive(false);
                optimizerHVACDevice.setEndTime(Instant.now());
                optimizerHVACDevice.setManual(true);
            }
            if(optimizerHVACDevice.getActive())
                active++;
        }
        Double currentClimateConsumption = currentTotalClimateConsumption / active;
        for(HVACDeviceDto hvacDeviceDto : hvacDeviceDtoList){
            OptimizerHVACDevice optimizerHVACDevice = getHvacDevicesCache().get(hvacDeviceDto.getEntityId());
            if(optimizerHVACDevice.getActive())
                optimizerHVACDevice.setActualPowerConsumption(currentClimateConsumption);;
        }
    }


}
