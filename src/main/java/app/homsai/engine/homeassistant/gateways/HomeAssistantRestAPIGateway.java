package app.homsai.engine.homeassistant.gateways;

import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantConfigDto;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantHistoryDto;

import java.time.Instant;
import java.util.List;

public interface HomeAssistantRestAPIGateway {

    List<HomeAssistantEntityDto> getAllHomeAssistantEntities();

    HomeAssistantConfigDto getHomeAssistantConfig();

    HomeAssistantEntityDto syncHomeAssistantEntityValue(String entityId);

    HomeAssistantEntityDto sendHomeAssistantClimateHVACMode(String entityId, String hvacMode);

    HomeAssistantEntityDto sendHomeAssistantClimateTemperature(String entityId, Double temperature);

    List<HomeAssistantHistoryDto> getHomeAssistantHistoryState(Instant startDatetime, Instant endDatetime, String entityId);

    HomeAssistantEntityDto sendHomeAssistantSwitchMode(String entityId, boolean on);
}
