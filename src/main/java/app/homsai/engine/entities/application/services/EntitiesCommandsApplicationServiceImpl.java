package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.cache.HomsaiHVACDeviceCacheRepository;
import app.homsai.engine.entities.application.http.converters.EntitiesMapper;
import app.homsai.engine.entities.application.http.dtos.HVACDeviceInitDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntitiesHistoricalStateDto;
import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import app.homsai.engine.entities.domain.models.*;
import app.homsai.engine.entities.domain.services.EntitiesCommandsService;
import app.homsai.engine.entities.domain.services.EntitiesQueriesService;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    HomsaiHVACDeviceCacheRepository homsaiHVACDeviceCacheRepository;

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
        logger.info("synchronizing homsai entities...");
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
            hvacDevice.setMaxTemp(homeAssistantEntityDto.getAttributes().getMaxTemp());
            hvacDevice.setMinTemp(homeAssistantEntityDto.getAttributes().getMinTemp());
            hvacDevice.setHvacModes(homeAssistantEntityDto.getAttributes().getHvacModes());
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

}
