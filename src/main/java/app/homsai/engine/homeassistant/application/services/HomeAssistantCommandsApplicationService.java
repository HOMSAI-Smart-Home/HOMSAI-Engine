package app.homsai.engine.homeassistant.application.services;

import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;

import java.util.List;

public interface HomeAssistantCommandsApplicationService {

    HomeAssistantEntityDto sendHomeAssistantClimateHVACMode(String climateEntityId, String homeAssistantHvacDeviceOffFunction);

    HomeAssistantEntityDto sendHomeAssistantClimateTemperature(String climateEntityId, Double temperature);

    HomeAssistantEntityDto sendHomeAssistantSwitchMode(String hvacSwitchEntityId, boolean on);
}
