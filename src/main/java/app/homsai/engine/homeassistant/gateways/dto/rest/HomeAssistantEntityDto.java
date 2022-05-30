
package app.homsai.engine.homeassistant.gateways.dto.rest;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "entity_id",
    "state",
    "attributes",
    "last_changed",
    "last_updated",
    "context"
})
@Generated("jsonschema2pojo")
public class HomeAssistantEntityDto {

    @JsonProperty("entity_id")
    private String entityId;
    @JsonProperty("state")
    private String state;
    @JsonProperty("attributes")
    private HomeAssistantAttributesDto attributes;
    @JsonProperty("last_changed")
    private String lastChanged;
    @JsonProperty("last_updated")
    private String lastUpdated;
    @JsonProperty("context")
    private HomeAssistantContextDto homeAssistantContextDto;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("last_changed")
    public String getLastChanged() {
        return lastChanged;
    }

    @JsonProperty("last_changed")
    public void setLastChanged(String lastChanged) {
        this.lastChanged = lastChanged;
    }

    @JsonProperty("last_updated")
    public String getLastUpdated() {
        return lastUpdated;
    }

    @JsonProperty("last_updated")
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty("context")
    public HomeAssistantContextDto getContext() {
        return homeAssistantContextDto;
    }

    @JsonProperty("context")
    public void setContext(HomeAssistantContextDto homeAssistantContextDto) {
        this.homeAssistantContextDto = homeAssistantContextDto;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
