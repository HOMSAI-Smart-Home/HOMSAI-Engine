package app.homsai.engine.optimizations.infrastructure.cache;

import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.application.http.cache.HomsaiEntityShowCacheRepository;
import app.homsai.engine.entities.application.http.dtos.AreaDto;
import app.homsai.engine.entities.application.http.dtos.HVACDeviceDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntityShowDto;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.optimizations.application.http.converters.OptimizationsMapper;
import app.homsai.engine.optimizations.domain.models.HvacDevice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static app.homsai.engine.common.domain.utils.Consts.HOME_ASSISTANT_WATT;


@Service
public class HVACRunningDevicesCacheRepositoryImpl implements HVACRunningDevicesCacheRepository {

    @Autowired
    private EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    @Autowired
    private HomeAssistantQueriesApplicationService homeAssistantQueriesApplicationService;

    @Autowired
    private HomsaiEntityShowCacheRepository homsaiEntityShowCacheRepository;

    @Autowired
    OptimizationsMapper optimizationsMapper;

    private static HashMap<String, HvacDevice> hvacDevicesCache = null;


    @Override
    public HashMap<String, HvacDevice> getHvacDevicesCache() {
        if(hvacDevicesCache == null)
            initHvacDevicesCache();
        return hvacDevicesCache;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetManualDevices() {
        for(HvacDevice hvacDevice : getHvacDevicesCache().values()){
            hvacDevice.setManual(false);
        }
    }

    //ToDo move business logic to service
    @Override
    public void initHvacDevicesCache() {
        hvacDevicesCache = new HashMap<>();
        List<HVACDeviceDto> hvacDeviceDtoList = entitiesQueriesApplicationService.getAllHomsaiHvacDevices(Consts.HVAC_DEVICE_CONDITIONING);
        List<AreaDto> areaList = entitiesQueriesApplicationService.getAllAreas();
        Double homeSetTemperature = areaList.stream()
                .filter(area -> area.getUuid().equals(Consts.HOME_AREA_UUID))
                .map(AreaDto::getDesiredSummerTemperature)
                .findFirst().orElse(null);
        for(HVACDeviceDto hvacDeviceDto : hvacDeviceDtoList){
            HvacDevice hvacDevice = new HvacDevice();
            hvacDevice.setEntityId(hvacDeviceDto.getEntityId());
            hvacDevice.setAreaId(hvacDeviceDto.getArea().getName());
            Double currentTemperature = homsaiEntityShowCacheRepository.getHomsaiEntityShowDtoList().stream()
                    .filter(hD -> hD.getArea().equals(hvacDeviceDto.getArea().getName()))
                    .map(HomsaiEntityShowDto::getTemperatureD)
                    .map(Optional::ofNullable)
                    .findFirst().flatMap(Function.identity()).orElse(null);
            hvacDevice.setCurrentTemperature(currentTemperature);
            Double setTemperature = areaList.stream()
                    .filter(area -> area.getName().equals(hvacDeviceDto.getArea().getName()))
                    .map(AreaDto::getDesiredSummerTemperature)
                    .map(Optional::ofNullable)
                    .findFirst().flatMap(Function.identity())
                    .orElse(null);
            if(setTemperature == null) setTemperature=homeSetTemperature;
            hvacDevice.setSetTemperature(setTemperature);
            hvacDevice.setActive(false);
            hvacDevice.setStartTime(null);
            hvacDevice.setEndTime(Instant.EPOCH);
            hvacDevice.setPowerConsumption(hvacDeviceDto.getPowerConsumption());
            hvacDevice.setIntervals(optimizationsMapper.convertToDtoIntervals(hvacDeviceDto.getIntervals()));
            if(hvacDeviceDto.getEnabled() == null)
                hvacDevice.setEnabled(false);
            else
                hvacDevice.setEnabled(hvacDeviceDto.getEnabled());
            hvacDevicesCache.put(hvacDevice.getEntityId(), hvacDevice);
        }
    }

    //ToDo move business logic to service
    @Override
    public void updateHvacDevicesCache(){
        List<HVACDeviceDto> hvacDeviceDtoList = entitiesQueriesApplicationService.getAllHomsaiHvacDevices(Consts.HVAC_DEVICE_CONDITIONING);
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        if(homeInfo.getHvacPowerMeterId() == null)
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
            HvacDevice hvacDevice = getHvacDevicesCache().get(hvacDeviceDto.getEntityId());
            if(hvacDeviceDto.getEnabled() == null)
                hvacDevice.setEnabled(false);
            else
                hvacDevice.setEnabled(hvacDeviceDto.getEnabled());
            hvacDevice.setIntervals(optimizationsMapper.convertToDtoIntervals(hvacDeviceDto.getIntervals()));
            Double currentTemperature = homsaiEntityShowCacheRepository.getHomsaiEntityShowDtoList().stream()
                    .filter(hD -> hD.getArea().equals(hvacDeviceDto.getArea().getName()))
                    .map(HomsaiEntityShowDto::getTemperatureD)
                    .map(Optional::ofNullable)
                    .findFirst().flatMap(Function.identity()).orElse(null);
            hvacDevice.setCurrentTemperature(currentTemperature);
            Double setTemperature = areaList.stream()
                    .filter(area -> area.getName().equals(hvacDeviceDto.getArea().getName()))
                    .map(AreaDto::getDesiredSummerTemperature)
                    .map(Optional::ofNullable)
                    .findFirst().flatMap(Function.identity())
                    .orElse(null);
            if(setTemperature == null) setTemperature=homeSetTemperature;
            hvacDevice.setSetTemperature(setTemperature);
            HomeAssistantEntityDto hvacDeviceEntity = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(hvacDevice.getEntityId());
            if(!hvacDevice.getActive() && !Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION.equals(hvacDeviceEntity.getState())){
                hvacDevice.setActive(true);
                hvacDevice.setStartTime(Instant.now());
                hvacDevice.setManual(true);
            } else if(hvacDevice.getActive() && Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION.equals(hvacDeviceEntity.getState())){
                hvacDevice.setActive(false);
                hvacDevice.setEndTime(Instant.now());
                hvacDevice.setManual(true);
            }
            if(hvacDevice.getActive())
                active++;
        }
        Double currentClimateConsumption = currentTotalClimateConsumption / active;
        for(HVACDeviceDto hvacDeviceDto : hvacDeviceDtoList){
            HvacDevice hvacDevice = getHvacDevicesCache().get(hvacDeviceDto.getEntityId());
            if(hvacDevice.getActive())
                hvacDevice.setActualPowerConsumption(currentClimateConsumption);;
        }
    }


}
