package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.converters.EntitiesMapper;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.services.EntitiesCommandsService;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.HomeAssistantWSAPIGateway;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntitiesCommandsApplicationServiceImpl implements EntitiesCommandsApplicationService {

    @Autowired
    HomeAssistantQueriesApplicationService homeAssistantQueriesApplicationService;

    @Autowired
    HomeAssistantWSAPIGateway homeAssistantWSAPIGateway;

    @Autowired
    EntitiesCommandsService entitiesCommandsService;

    @Autowired
    EntitiesMapper entitiesMapper;




    @Override
    public void syncHomeAssistantEntities() throws InterruptedException {
        List<HomeAssistantEntityDto> homeAssistantEntityDtoList = homeAssistantQueriesApplicationService.getHomeAssistantEntities(null);
        List<HAEntity> haEntityList = entitiesMapper.convertFromDto(homeAssistantEntityDtoList);
        if(haEntityList.size() > 0){
            entitiesCommandsService.truncateHomsaiEntities();
            entitiesCommandsService.truncateHAEntities();
        }
        List<HAEntity> haEntitySavedList = entitiesCommandsService.saveAllHAEntities(haEntityList);
        Object lock = new Object();
        homeAssistantWSAPIGateway.syncEntityAreas(haEntitySavedList, lock);
        synchronized (lock) {
            lock.wait(30000);
        }
        entitiesCommandsService.syncHomsaiEntities();
    }
}
