package app.homsai.engine.homeassistant.domain.services;

import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.homeassistant.gateways.HomeAssistantWSAPIGateway;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantConfigDto;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.homeassistant.gateways.HomeAssistantRestAPIGateway;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static app.homsai.engine.homeassistant.gateways.HomeAssistantDomains.CLIMATE;


@Service
public class HomeAssistantQueriesServiceImpl implements HomeAssistantQueriesService {

    @Autowired
    HomeAssistantRestAPIGateway homeAssistantRestAPIGateway;

    @Autowired
    HomeAssistantWSAPIGateway homeAssistantWSAPIGateway;

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

    @Override
    public List<HomeAssistantHistoryDto> getHomeAssistantHistoryState(Instant startDate, Instant endDate, String entityId) {
        return homeAssistantRestAPIGateway.getHomeAssistantHistoryState(startDate, endDate, entityId);
    }

    @Override
    public HomeAssistantConfigDto getConfig() {
        return homeAssistantRestAPIGateway.getHomeAssistantConfig();
    }
}
