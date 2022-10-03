package app.homsai.engine.homeassistant.application.services;

import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantConfigDto;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.homeassistant.domain.services.HomeAssistantQueriesService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class HomeAssistantQueriesApplicationServiceImpl implements HomeAssistantQueriesApplicationService {

    @Autowired
    HomeAssistantQueriesService homeAssistantQueriesService;

    @Override
    public List<HomeAssistantEntityDto> getHomeAssistantEntities(String domain) {
        return homeAssistantQueriesService.getHomeAssistantEntities(domain);
    }
    @Override
    public void syncEntityAreas(List<HAEntity> entityList, Object lock){
        homeAssistantQueriesService.syncEntityAreas(entityList, lock);
    }

    @Override
    public HomeAssistantEntityDto syncHomeAssistantEntityValue(String entityId) {
        return homeAssistantQueriesService.syncHomeAssistantEntityValue(entityId);
    }

    @Override
    public List<HomeAssistantHistoryDto> getHomeAssistantHistoryState(Instant startDate, Instant endDate, String entityId) {
        return homeAssistantQueriesService.getHomeAssistantHistoryState(startDate, endDate, entityId);
    }

    @Override
    public HomeAssistantConfigDto getConfig() {
        return homeAssistantQueriesService.getConfig();
    }
}
