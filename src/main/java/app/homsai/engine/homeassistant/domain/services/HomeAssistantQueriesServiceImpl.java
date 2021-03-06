package app.homsai.engine.homeassistant.domain.services;

import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.homeassistant.gateways.HomeAssistantWSAPIGatewayImpl;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.homeassistant.gateways.HomeAssistantRestAPIGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static app.homsai.engine.homeassistant.gateways.HomeAssistantDomains.CLIMATE;


@Service
public class HomeAssistantQueriesServiceImpl implements HomeAssistantQueriesService {

    @Autowired
    HomeAssistantRestAPIGateway homeAssistantRestAPIGateway;

    @Autowired
    HomeAssistantWSAPIGatewayImpl homeAssistantWSAPIGateway;

    @Override
    public List<HomeAssistantEntityDto> getHomeAssistantEntities(String domain) {
        List<HomeAssistantEntityDto> entities = homeAssistantRestAPIGateway.getAllHomeAssistantEntities();
        if(domain == null)
            return entities;
        return entities.stream().filter(e -> e.getEntityId().contains(domain)).collect(Collectors.toList());
    }

    @Override
    public List<HomeAssistantEntityDto> getHomeAssistantClimateEntitiesHavingMode(String mode) {
        List<HomeAssistantEntityDto> entities = getHomeAssistantEntities(CLIMATE);
        return entities.stream().filter(e -> e.getAttributes().getHvacModes().contains(mode)).collect(Collectors.toList());
    }

    @Override
    public void syncEntityAreas(List<HAEntity> entityList, Object lock) {
        homeAssistantWSAPIGateway.syncEntityAreas(entityList, lock);
    }

    @Override
    public HomeAssistantEntityDto syncHomeAssistantEntityValue(String entityId) {
        return homeAssistantRestAPIGateway.syncHomeAssistantEntityValue(entityId);
    }
}
