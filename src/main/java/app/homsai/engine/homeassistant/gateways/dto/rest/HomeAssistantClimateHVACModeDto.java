package app.homsai.engine.homeassistant.gateways.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HomeAssistantClimateHVACModeDto {

    @JsonProperty("entity_id")
    private String entityId;

    @JsonProperty("hvac_mode")
    private String hvacMode;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getHvacMode() {
        return hvacMode;
    }

    public void setHvacMode(String hvacMode) {
        this.hvacMode = hvacMode;
    }
}
