package app.homsai.engine.homeassistant.gateways.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * @author Giacomo Agostini on 03/10/2022
 */
public class HomeAssistantHistoryDto {

    @JsonProperty("entity_id")
    private String entityId;
    @JsonProperty("state")
    private String state;
    @JsonProperty("attributes")
    private HomeAssistantAttributesDto attributes;
    @JsonProperty("last_changed")
    private Instant lastChanged;
    @JsonProperty("last_updated")
    private Instant lastUpdated;

    @JsonProperty("entity_id")
    public String getEntityId() {
        return entityId;
    }

    @JsonProperty("entity_id")
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("attributes")
    public HomeAssistantAttributesDto getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(HomeAssistantAttributesDto attributes) {
        this.attributes = attributes;
    }

    public Instant getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Instant lastChanged) {
        this.lastChanged = lastChanged;
    }

    @JsonProperty("last_updated")
    public Instant getLastUpdated() {
        return lastUpdated;
    }

    @JsonProperty("last_updated")
    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
