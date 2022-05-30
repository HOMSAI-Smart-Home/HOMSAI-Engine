
package app.homsai.engine.homeassistant.gateways.dto.ws;

import java.util.HashMap;
import java.util.List;
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
    "entity",
    "device",
    "config_entry",
    "area"
})
@Generated("jsonschema2pojo")
public class HomeAssistantWSSearchRelatedResultResponseDto {

    @JsonProperty("entity")
    private List<String> entity = null;
    @JsonProperty("device")
    private List<String> device = null;
    @JsonProperty("config_entry")
    private List<String> configEntry = null;
    @JsonProperty("area")
    private List<String> area = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("entity")
    public List<String> getEntity() {
        return entity;
    }

    @JsonProperty("entity")
    public void setEntity(List<String> entity) {
        this.entity = entity;
    }

    @JsonProperty("device")
    public List<String> getDevice() {
        return device;
    }

    @JsonProperty("device")
    public void setDevice(List<String> device) {
        this.device = device;
    }

    @JsonProperty("config_entry")
    public List<String> getConfigEntry() {
        return configEntry;
    }

    @JsonProperty("config_entry")
    public void setConfigEntry(List<String> configEntry) {
        this.configEntry = configEntry;
    }

    @JsonProperty("area")
    public List<String> getArea() {
        return area;
    }

    @JsonProperty("area")
    public void setArea(List<String> area) {
        this.area = area;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
