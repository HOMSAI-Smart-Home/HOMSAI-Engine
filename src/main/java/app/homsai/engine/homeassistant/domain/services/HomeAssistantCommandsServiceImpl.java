package app.homsai.engine.homeassistant.domain.services;


import app.homsai.engine.homeassistant.gateways.HomeAssistantWSAPIGateway;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HomeAssistantCommandsServiceImpl implements HomeAssistantCommandsService {

    @Autowired
    private HomeAssistantQueriesService homeAssistantQueriesService;

    @Autowired
    private HomeAssistantWSAPIGateway homeAssistantWSAPIGateway;


    @Override
    public void syncHomeAssistantEntities() {
        List<HomeAssistantEntityDto> entities = homeAssistantQueriesService.getHomeAssistantEntities(null);


    }
}
