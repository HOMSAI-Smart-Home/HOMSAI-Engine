package app.homsai.engine.homeassistant.application.services;

import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantConfigDto;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantHistoryDto;

import java.time.Instant;
import java.util.List;

public interface HomeAssistantQueriesApplicationService {

    List<HomeAssistantEntityDto> getHomeAssistantEntities(String domain);

    void syncEntityAreas(List<HAEntity> entityList, Object lock);

    HomeAssistantEntityDto syncHomeAssistantEntityValue(String entityId);

    List<HomeAssistantHistoryDto> getHomeAssistantHistoryState(Instant startDate, Instant endDate, String generalPowerMeterId);

    HomeAssistantConfigDto getConfig();
}
