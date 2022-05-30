package app.homsai.engine.homeassistant.domain.services;


import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;

import java.util.List;

public interface HomeAssistantQueriesService {


    List<HomeAssistantEntityDto> getHomeAssistantEntities(String domain);

    List<HomeAssistantEntityDto> getHomeAssistantClimateEntitiesHavingMode(String mode);
}
