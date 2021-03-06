package app.homsai.engine.entities.application.services;

import app.homsai.engine.common.domain.exceptions.BadRequestException;
import app.homsai.engine.common.domain.models.ErrorCodes;
import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.application.http.converters.EntitiesMapper;
import app.homsai.engine.entities.application.http.dtos.HVACDeviceInitDto;
import app.homsai.engine.entities.application.http.dtos.HomeHvacSettingsDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntitiesHistoricalStateDto;
import app.homsai.engine.entities.application.http.dtos.HvacDeviceSettingDto;
import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.exceptions.BadIntervalsException;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import app.homsai.engine.entities.domain.models.*;
import app.homsai.engine.entities.domain.services.EntitiesCommandsService;
import app.homsai.engine.entities.domain.services.EntitiesQueriesService;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.optimizations.infrastructure.cache.HVACRunningDevicesCacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static app.homsai.engine.common.domain.utils.Consts.*;


@Service
public class EntitiesCommandsApplicationServiceImpl implements EntitiesCommandsApplicationService {

    @Autowired
    HomeAssistantQueriesApplicationService homeAssistantQueriesApplicationService;

    @Autowired
    EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    @Autowired
    EntitiesCommandsService entitiesCommandsService;

    @Autowired
    EntitiesQueriesService entitiesQueriesService;

    @Autowired
    EntitiesMapper entitiesMapper;

    @Autowired
    HVACRunningDevicesCacheRepository hvacRunningDevicesCacheRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @Override
    public void syncHomeAssistantEntities() throws InterruptedException {
        logger.info("synchronizing home assistant entities...");
        List<HomeAssistantEntityDto> homeAssistantEntityDtoList = homeAssistantQueriesApplicationService.getHomeAssistantEntities(null);
        List<HAEntity> haEntityList = entitiesMapper.convertFromDto(homeAssistantEntityDtoList);
        if(haEntityList.size() > 0){
            entitiesCommandsService.truncateHomsaiEntities();
            entitiesCommandsService.truncateHAEntities();
        }
        List<HAEntity> haEntitySavedList = entitiesCommandsService.saveAllHAEntities(haEntityList);
        Object lock = new Object();
        homeAssistantQueriesApplicationService.syncEntityAreas(haEntitySavedList, lock);
        synchronized (lock) {
            lock.wait(30000);
        }
        Integer homsaiEntitiesCount = entitiesCommandsService.syncHomsaiEntities();
        logger.info("synchronized "+haEntitySavedList.size()+ " Home Assistant entities and "+homsaiEntitiesCount+ " Homsai entities");
    }

    @Override
    public List<HomsaiEntitiesHistoricalStateDto> syncHomsaiEntitiesValues() throws AreaNotFoundException {
        logger.debug("synchronizing homsai entities...");
        List<HomsaiEntity> homsaiEntityList = entitiesQueriesService.findAllHomsaiEntities(Pageable.unpaged(), null).getContent();
        List<HomsaiEntitiesHistoricalState> homsaiEntitiesHistoricalStateList = entitiesCommandsService.calculateHomsaiEntitiesValues(homsaiEntityList);
        List<HomsaiEntitiesHistoricalState> homsaiHomeHistoricalStateList = entitiesCommandsService.calculateHomsaiHomeValues(homsaiEntitiesHistoricalStateList);
        logger.info("synchronized "+(homsaiEntitiesHistoricalStateList.size()+ homsaiHomeHistoricalStateList.size())+" Homsai entities values");
        entitiesQueriesApplicationService.cacheAllLastHomsaiEntitiesToShow();
        return entitiesMapper.convertHistoricalListToDto(homsaiEntitiesHistoricalStateList);
    }

    @Override
    public void addExcludedHAEntities(List<String> excludedIds) throws InterruptedException {
        for(String excludeEntityId : excludedIds){
            ExcludedHAEntity excludedHAEntity = new ExcludedHAEntity(excludeEntityId);
            entitiesCommandsService.saveExcludedHAEntity(excludedHAEntity);
        }
        syncHomeAssistantEntities();
    }

    @Override
    @Transactional
    public HVACDeviceInitDto initHVACDevices(Integer type) throws InterruptedException, HvacPowerMeterIdNotSet {
        HomeInfo homeInfo = entitiesQueriesService.findHomeInfo();
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
            entitiesCommandsService.deleteFromHvacDevicesByType(type);
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
        entitiesCommandsService.initHomsaiHvacDevices(hvacDeviceList, hvacFunction);
        HVACDeviceInitDto hvacDeviceInitDto = new HVACDeviceInitDto();
        hvacDeviceInitDto.setHvacDeviceDtoList(entitiesMapper.convertToDto(hvacDeviceList));
        hvacDeviceInitDto.setInitTimeSecs(entitiesCommandsService.calculateInitTime(hvacDeviceList.size()).intValue());
        return hvacDeviceInitDto;
    }

    @Override
    public Integer getHvacDeviceInitTimeSeconds(){
        return entitiesCommandsService.calculateInitTime((int) entitiesQueriesService.findAllEntities(Pageable.unpaged(), "domain:climate").getTotalElements()).intValue();
    }

    @Override
    public void saveHomeInfo(HomeInfo homeInfo) {
        try {
            entitiesCommandsService.updateHomeInfo(homeInfo);
        } catch (BadHomeInfoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HvacDeviceSettingDto updateHvacDeviceSetting(String hvacDeviceEntityId, HvacDeviceSettingDto hvacDeviceSettingDto) throws BadIntervalsException {
        HVACDevice hvacDevice = entitiesQueriesService.findOneHvacDeviceByEntityId(hvacDeviceEntityId);
        Boolean enabledValue = hvacDeviceSettingDto.getEnabled();
        Boolean autoMode = hvacDeviceSettingDto.getManual();
        Double desiredTemperature = hvacDeviceSettingDto.getSetTemperature();
        LocalTime startTimeValue = hvacDeviceSettingDto.getStartTime();
        LocalTime endTimeValue = hvacDeviceSettingDto.getEndTime();
        hvacDevice.setEnabled(enabledValue);
        hvacRunningDevicesCacheRepository.getHvacDevicesCache().get(hvacDevice.getEntityId()).setManual(autoMode);
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
        entitiesCommandsService.updateHvacDevice(hvacDevice);
        hvacRunningDevicesCacheRepository.updateHvacDevicesCache();
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
