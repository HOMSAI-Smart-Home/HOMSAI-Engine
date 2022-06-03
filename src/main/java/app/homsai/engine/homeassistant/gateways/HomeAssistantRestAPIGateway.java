package app.homsai.engine.homeassistant.gateways;

import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;

import java.util.List;

public interface HomeAssistantRestAPIGateway {

    List<HomeAssistantEntityDto> getAllHomeAssistantEntities();

    HomeAssistantEntityDto syncHomeAssistantEntityValue(String entityId);
}
