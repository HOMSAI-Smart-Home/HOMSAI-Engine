package app.homsai.engine.homeassistant.domain.services;

import app.homsai.engine.homeassistant.application.http.dtos.HomeAssistantEntityDto;
import app.homsai.engine.homeassistant.gateways.HomeAssistantGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static app.homsai.engine.homeassistant.gateways.HomeAssistantDomains.CLIMATE;


@Service
public class HomeAssistantQueriesServiceImpl implements HomeAssistantQueriesService {

    @Autowired
    HomeAssistantGateway homeAssistantGateway;

    @Override
    public List<HomeAssistantEntityDto> getHomeAssistantEntities(String domain) {
        List<HomeAssistantEntityDto> entities = homeAssistantGateway.getAllHomeAssistantEntities();
        if(domain == null)
            return entities;
        return entities.stream().filter(e -> e.getEntityId().contains(domain)).collect(Collectors.toList());
    }

    @Override
    public List<HomeAssistantEntityDto> getHomeAssistantClimateEntitiesHavingMode(String mode) {
        List<HomeAssistantEntityDto> entities = getHomeAssistantEntities(CLIMATE);
        return entities.stream().filter(e -> e.getAttributes().getHvacModes().contains(mode)).collect(Collectors.toList());
    }
}
