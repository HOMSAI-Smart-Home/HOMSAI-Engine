package app.homsai.engine.pvoptimizer.application.services;

import app.homsai.engine.common.domain.exceptions.BadRequestException;
import app.homsai.engine.common.domain.models.ErrorCodes;
import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.exceptions.BadIntervalsException;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.entities.domain.services.EntitiesCommandsService;
import app.homsai.engine.entities.domain.services.EntitiesQueriesService;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.pvoptimizer.application.http.converters.PVOptimizerMapper;
import app.homsai.engine.pvoptimizer.application.http.dtos.*;
import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import app.homsai.engine.pvoptimizer.domain.models.HvacDeviceInterval;
import app.homsai.engine.pvoptimizer.domain.services.PVOptimizerCommandsService;
import app.homsai.engine.pvoptimizer.domain.services.PVOptimizerQueriesService;
import app.homsai.engine.pvoptimizer.domain.services.cache.PVOptimizerCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static app.homsai.engine.common.domain.utils.Consts.*;
import static app.homsai.engine.common.domain.utils.Consts.HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION;


@Service
public class PVOptimizerCommandsApplicationServiceImpl implements PVOptimizerCommandsApplicationService {

    @Autowired
    EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    @Autowired
    HomeAssistantQueriesApplicationService homeAssistantQueriesApplicationService;

    @Autowired
    EntitiesQueriesService entitiesQueriesService;

    @Autowired
    EntitiesCommandsService entitiesCommandsService;

    @Autowired
    PVOptimizerMapper pvOptimizerMapper;

    @Autowired
    PVOptimizerCommandsService pvOptimizerCommandsService;

    @Autowired
    PVOptimizerQueriesService pvOptimizerQueriesService;

    @Autowired
    PVOptimizerCacheService pvOptimizerCacheService;

    @Override
    @Transactional
    public HVACDeviceInitDto initHVACDevices(Integer type) throws InterruptedException, HvacPowerMeterIdNotSet {
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        if(homeInfo.getHvacPowerMeterId() == null)
            throw new HvacPowerMeterIdNotSet();
        List<HVACDevice> hvacDeviceList = new ArrayList<>();
        String hvacFunction;
        switch (type){
            case HVAC_DEVICE_HEATING:
                hvacFunction = HOME_ASSISTANT_HVAC_DEVICE_HEATING_FUNCTION;
                break;
            case HVAC_DEVICE_CONDITIONING:
            default:
                hvacFunction = HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION;
        }
        List<HomeAssistantEntityDto> homeAssistantEntityDtoList = homeAssistantQueriesApplicationService.getHomeAssistantEntities("climate");
        List<HAEntity> haEntities = entitiesQueriesService.findAllEntities(Pageable.unpaged(), "domain:climate").getContent();
        if(homeAssistantEntityDtoList.size()>0)
            pvOptimizerCommandsService.deleteFromHvacDevicesByType(type);
        for(HomeAssistantEntityDto homeAssistantEntityDto : homeAssistantEntityDtoList){
            if(homeAssistantEntityDto.getAttributes().getHvacModes() == null || !homeAssistantEntityDto.getAttributes().getHvacModes().contains(hvacFunction))
                continue;
            HAEntity haEntity = haEntities.stream()
                    .filter(h -> h.getEntityId().equals(homeAssistantEntityDto.getEntityId()))
                    .findFirst()
                    .orElse(null);
            if(haEntity == null || haEntity.getAreas() == null || haEntity.getAreas().size() < 1)
                continue;
            Area area = haEntity.getAreas().iterator().next();
            HVACDevice hvacDevice = new HVACDevice();
            hvacDevice.setArea(area);
            hvacDevice.setEntityId(homeAssistantEntityDto.getEntityId());
            hvacDevice.setType(type);
            try {
                hvacDevice.setMaxTemp(Double.parseDouble(homeAssistantEntityDto.getAttributes().getMaxTemp()));
                hvacDevice.setMinTemp(Double.parseDouble(homeAssistantEntityDto.getAttributes().getMinTemp()));
            }catch (Exception e){
                hvacDevice.setMaxTemp(30D);
                hvacDevice.setMinTemp(16D);
            }
            hvacDevice.setHvacModes(homeAssistantEntityDto.getAttributes().getHvacModes());
            hvacDevice.setEnabled(true);
            hvacDeviceList.add(hvacDevice);
        }
        pvOptimizerCommandsService.initHomsaiHvacDevices(hvacDeviceList, hvacFunction);
        HVACDeviceInitDto hvacDeviceInitDto = new HVACDeviceInitDto();
        hvacDeviceInitDto.setHvacDeviceDtoList(pvOptimizerMapper.convertToDto(hvacDeviceList));
        hvacDeviceInitDto.setInitTimeSecs(pvOptimizerCommandsService.calculateInitTime(hvacDeviceList.size()).intValue());
        return hvacDeviceInitDto;
    }

    @Override
    public Integer getHvacDeviceInitTimeSeconds(){
        return pvOptimizerCommandsService.calculateInitTime((int) entitiesQueriesService.findAllEntities(Pageable.unpaged(), "domain:climate").getTotalElements()).intValue();
    }


    @Override
    public HvacDeviceSettingDto updateHvacDeviceSetting(String hvacDeviceEntityId, HvacDeviceSettingDto hvacDeviceSettingDto) throws BadIntervalsException {
        HVACDevice hvacDevice = pvOptimizerQueriesService.findOneHvacDeviceByEntityId(hvacDeviceEntityId);
        Boolean enabledValue = hvacDeviceSettingDto.getEnabled();
        Boolean autoMode = hvacDeviceSettingDto.getManual();
        Double desiredTemperature = hvacDeviceSettingDto.getSetTemperature();
        LocalTime startTimeValue = hvacDeviceSettingDto.getStartTime();
        LocalTime endTimeValue = hvacDeviceSettingDto.getEndTime();
        hvacDevice.setEnabled(enabledValue);
        pvOptimizerCacheService.getHvacDevicesCache().get(hvacDevice.getEntityId()).setManual(autoMode);
        hvacDevice.getArea().setDesiredSummerTemperature(desiredTemperature);
        if(startTimeValue != null && endTimeValue != null) {
            HvacDeviceInterval hvacInterval = new HvacDeviceInterval(startTimeValue, endTimeValue);
            List<HvacDeviceInterval> hvacDeviceIntervalList = new ArrayList<>();
            hvacDeviceIntervalList.add(hvacInterval);
            hvacDevice.setIntervals(hvacDeviceIntervalList);
        }
        else if(startTimeValue == null && endTimeValue == null)
            hvacDevice.setIntervals(null);
        else throw new BadIntervalsException();
        pvOptimizerCommandsService.updateHvacDevice(hvacDevice);
        pvOptimizerCacheService.updateHvacDevicesCache();
        return hvacDeviceSettingDto;
    }

    @Override
    public HomeHvacSettingsDto updateHomeHvacSettings(HomeHvacSettingsDto homeHvacSettingsDto) throws BadRequestException, BadHomeInfoException {
        if (homeHvacSettingsDto.getOptimizerEnabled() == null || homeHvacSettingsDto.getSetTemperature() == null)
            throw new BadRequestException(ErrorCodes.BAD_HOME_INFO, "Home settings cannot be null");
        HomeInfo homeInfo = entitiesQueriesService.findHomeInfo();
        Area area = entitiesQueriesService.getHomeArea();
        if(Consts.HVAC_MODE.equals("summer")){
            area.setDesiredSummerTemperature(homeHvacSettingsDto.getSetTemperature());
        } else {
            area.setDesiredWinterTemperature(homeHvacSettingsDto.getSetTemperature());
        }
        homeInfo.setPvOptimizationsEnabled(homeHvacSettingsDto.getOptimizerEnabled());
        entitiesCommandsService.updateHomeInfo(homeInfo);
        entitiesCommandsService.updateArea(area);
        return homeHvacSettingsDto;
    }



}
