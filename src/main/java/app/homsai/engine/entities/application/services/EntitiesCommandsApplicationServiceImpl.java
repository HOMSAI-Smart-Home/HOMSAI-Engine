package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.converters.EntitiesMapper;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntitiesHistoricalStateDto;
import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.models.HomsaiEntitiesHistoricalState;
import app.homsai.engine.entities.domain.models.HomsaiEntity;
import app.homsai.engine.entities.domain.services.EntitiesCommandsService;
import app.homsai.engine.entities.domain.services.EntitiesQueriesService;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class EntitiesCommandsApplicationServiceImpl implements EntitiesCommandsApplicationService {

    @Autowired
    HomeAssistantQueriesApplicationService homeAssistantQueriesApplicationService;

    @Autowired
    EntitiesCommandsService entitiesCommandsService;

    @Autowired
    EntitiesQueriesService entitiesQueriesService;

    @Autowired
    EntitiesMapper entitiesMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @Override
    //@Scheduled(cron = "0 0 0 * * ?")
    //@EventListener(ApplicationStartedEvent.class)
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
    //@Scheduled(fixedRate = 5*60*1000)
    public List<HomsaiEntitiesHistoricalStateDto> syncHomsaiEntitiesValues() throws AreaNotFoundException {
        logger.info("synchronizing homsai entities...");
        List<HomsaiEntity> homsaiEntityList = entitiesQueriesService.findAllHomsaiEntities(Pageable.unpaged(), null).getContent();
        List<HomsaiEntitiesHistoricalState> homsaiEntitiesHistoricalStateList = entitiesCommandsService.calculateHomsaiEntitiesValues(homsaiEntityList);
        List<HomsaiEntitiesHistoricalState> homsaiHomeHistoricalStateList = entitiesCommandsService.calculateHomsaiHomeValues(homsaiEntitiesHistoricalStateList);
        logger.info("synchronized "+(homsaiEntitiesHistoricalStateList.size()+ homsaiHomeHistoricalStateList.size())+" Homsai entities values");
        return entitiesMapper.convertHistoricalListToDto(homsaiEntitiesHistoricalStateList);
    }

}
