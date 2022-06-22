package app.homsai.engine.homeassistant.gateways.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HomeAssistantClimateSetTemperatureDto {

    @JsonProperty("entity_id")
    private String entityId;

    @JsonProperty("temperature")
    private Double temperature;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}
