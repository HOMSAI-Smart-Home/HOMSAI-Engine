package app.homsai.engine.homeassistant.gateways;

import app.homsai.engine.homeassistant.application.http.dtos.HomeAssistantEntityDto;

import java.util.List;

public interface HomeAssistantGateway {

    List<HomeAssistantEntityDto> getAllHomeAssistantEntities();
}
