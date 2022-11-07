package app.homsai.engine.homeassistant.gateways.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HomeAssistantSwitchModeDto {

    @JsonProperty("entity_id")
    private String entityId;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
