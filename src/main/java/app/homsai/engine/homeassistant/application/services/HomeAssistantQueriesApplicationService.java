package app.homsai.engine.homeassistant.application.services;

import app.homsai.engine.homeassistant.application.http.dtos.HomeAssistantEntityDto;

import java.util.List;

public interface HomeAssistantQueriesApplicationService {

    List<HomeAssistantEntityDto> getHomeAssistantEntities(String climate);
}
