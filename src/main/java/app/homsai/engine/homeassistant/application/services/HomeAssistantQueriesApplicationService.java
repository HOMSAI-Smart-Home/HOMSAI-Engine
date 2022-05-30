package app.homsai.engine.homeassistant.application.services;

import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;

import java.util.List;

public interface HomeAssistantQueriesApplicationService {

    List<HomeAssistantEntityDto> getHomeAssistantEntities(String domain);
}
