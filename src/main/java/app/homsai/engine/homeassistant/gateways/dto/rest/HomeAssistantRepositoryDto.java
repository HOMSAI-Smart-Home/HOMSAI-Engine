
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
    "name",
    "display_name",
    "installed_version",
    "available_version"
})
@Generated("jsonschema2pojo")
public class HomeAssistantRepositoryDto {

    @JsonProperty("name")
    private String name;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("installed_version")
    private String installedVersion;
    @JsonProperty("available_version")
    private String availableVersion;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("display_name")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("display_name")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("installed_version")
    public String getInstalledVersion() {
        return installedVersion;
    }

    @JsonProperty("installed_version")
    public void setInstalledVersion(String installedVersion) {
        this.installedVersion = installedVersion;
    }

    @JsonProperty("available_version")
    public String getAvailableVersion() {
        return availableVersion;
    }

    @JsonProperty("available_version")
    public void setAvailableVersion(String availableVersion) {
        this.availableVersion = availableVersion;
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
