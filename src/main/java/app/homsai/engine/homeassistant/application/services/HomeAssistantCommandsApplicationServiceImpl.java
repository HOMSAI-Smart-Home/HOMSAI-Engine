package app.homsai.engine.homeassistant.application.services;

import app.homsai.engine.homeassistant.domain.services.HomeAssistantCommandsService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeAssistantCommandsApplicationServiceImpl implements HomeAssistantCommandsApplicationService {

    @Autowired
    HomeAssistantCommandsService homeAssistantCommandsService;

    @Override
    public HomeAssistantEntityDto sendHomeAssistantClimateHVACMode(String climateEntityId, String homeAssistantHvacDeviceOffFunction) {
        return homeAssistantCommandsService.sendHomeAssistantClimateHVACMode( climateEntityId, homeAssistantHvacDeviceOffFunction);
    }

    @Override
    public HomeAssistantEntityDto sendHomeAssistantClimateTemperature(String climateEntityId, Double temperature) {
        return homeAssistantCommandsService.sendHomeAssistantClimateTemperature( climateEntityId, temperature);
    }

    @Override
    public HomeAssistantEntityDto sendHomeAssistantSwitchMode(String hvacSwitchEntityId, boolean on) {
        return homeAssistantCommandsService.sendHomeAssistantSwitchMode(hvacSwitchEntityId, on);
    }

}
