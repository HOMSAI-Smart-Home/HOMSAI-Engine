package app.homsai.engine.homeassistant.domain.services;


import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;

import java.util.List;

public interface HomeAssistantQueriesService {


    List<HomeAssistantEntityDto> getHomeAssistantEntities(String domain);

    List<HomeAssistantEntityDto> getHomeAssistantClimateEntitiesHavingMode(String mode);

    void syncEntityAreas(List<HAEntity> entityList, Object lock);

    HomeAssistantEntityDto syncHomeAssistantEntityValue(String entityId);
}
