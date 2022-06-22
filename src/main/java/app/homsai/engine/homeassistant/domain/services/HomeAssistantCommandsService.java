package app.homsai.engine.homeassistant.domain.services;


import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;

public interface HomeAssistantCommandsService {

    void syncHomeAssistantEntities();

    HomeAssistantEntityDto sendHomeAssistantClimateHVACMode(String climateEntityId, String homeAssistantHvacDeviceOffFunction);

    HomeAssistantEntityDto sendHomeAssistantClimateTemperature(String climateEntityId, Double temperature);
}
