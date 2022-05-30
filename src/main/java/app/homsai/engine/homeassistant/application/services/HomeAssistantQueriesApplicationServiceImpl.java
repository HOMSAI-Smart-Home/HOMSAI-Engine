package app.homsai.engine.homeassistant.application.services;

import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.homeassistant.domain.services.HomeAssistantQueriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeAssistantQueriesApplicationServiceImpl implements HomeAssistantQueriesApplicationService {

    @Autowired
    HomeAssistantQueriesService homeAssistantQueriesService;

    @Override
    public List<HomeAssistantEntityDto> getHomeAssistantEntities(String domain) {
        return homeAssistantQueriesService.getHomeAssistantEntities(domain);
    }
}
