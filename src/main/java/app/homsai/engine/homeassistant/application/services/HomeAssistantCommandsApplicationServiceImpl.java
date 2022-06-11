package app.homsai.engine.homeassistant.application.services;

import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.homeassistant.domain.services.HomeAssistantCommandsService;
import app.homsai.engine.homeassistant.domain.services.HomeAssistantQueriesService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeAssistantCommandsApplicationServiceImpl implements HomeAssistantCommandsApplicationService {

    @Autowired
    HomeAssistantCommandsService homeAssistantCommandsService;

    @Override
    public HomeAssistantEntityDto sendHomeAssistantClimateHVACMode(String climateEntityId, String homeAssistantHvacDeviceOffFunction) {
        return homeAssistantCommandsService.sendHomeAssistantClimateHVACMode( climateEntityId, homeAssistantHvacDeviceOffFunction);
    }
}
