package app.homsai.engine.homeassistant.domain.services;


import app.homsai.engine.homeassistant.application.http.dtos.HomeAssistantEntityDto;

import java.util.List;

public interface HomeAssistantQueriesService {


    List<HomeAssistantEntityDto> getHomeAssistantEntities(String domain);

    List<HomeAssistantEntityDto> getHomeAssistantClimateEntitiesHavingMode(String mode);
}
