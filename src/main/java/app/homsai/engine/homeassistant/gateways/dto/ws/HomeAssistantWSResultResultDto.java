package app.homsai.engine.homeassistant.gateways.dto.ws;
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
        "area_id",
        "config_entry_id",
        "device_id",
        "disabled_by",
        "entity_category",
        "entity_id",
        "hidden_by",
        "icon",
        "name",
        "platform"
})
@Generated("jsonschema2pojo")
public class HomeAssistantWSResultResultDto {

    @JsonProperty("area_id")
    private String areaId;
    @JsonProperty("config_entry_id")
    private String configEntryId;
    @JsonProperty("device_id")
    private String deviceId;
    @JsonProperty("disabled_by")
    private String disabledBy;
    @JsonProperty("entity_category")
    private String entityCategory;
    @JsonProperty("entity_id")
    private String entityId;
    @JsonProperty("hidden_by")
    private String hiddenBy;
    @JsonProperty("icon")
    private String icon;
    @JsonProperty("name")
    private String name;
    @JsonProperty("platform")
    private String platform;
    @JsonIgnore
    private Map<String, String> additionalProperties = new HashMap<String, String>();

    @JsonProperty("area_id")
    public String getAreaId() {
        return areaId;
    }

    @JsonProperty("area_id")
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @JsonProperty("config_entry_id")
    public String getConfigEntryId() {
        return configEntryId;
    }

    @JsonProperty("config_entry_id")
    public void setConfigEntryId(String configEntryId) {
        this.configEntryId = configEntryId;
    }

    @JsonProperty("device_id")
    public String getDeviceId() {
        return deviceId;
    }

    @JsonProperty("device_id")
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @JsonProperty("disabled_by")
    public String getDisabledBy() {
        return disabledBy;
    }

    @JsonProperty("disabled_by")
    public void setDisabledBy(String disabledBy) {
        this.disabledBy = disabledBy;
    }

    @JsonProperty("entity_category")
    public String getEntityCategory() {
        return entityCategory;
    }

    @JsonProperty("entity_category")
    public void setEntityCategory(String entityCategory) {
        this.entityCategory = entityCategory;
    }

    @JsonProperty("entity_id")
    public String getEntityId() {
        return entityId;
    }

    @JsonProperty("entity_id")
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @JsonProperty("hidden_by")
    public String getHiddenBy() {
        return hiddenBy;
    }

    @JsonProperty("hidden_by")
    public void setHiddenBy(String hiddenBy) {
        this.hiddenBy = hiddenBy;
    }

    @JsonProperty("icon")
    public String getIcon() {
        return icon;
    }

    @JsonProperty("icon")
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("platform")
    public String getPlatform() {
        return platform;
    }

    @JsonProperty("platform")
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @JsonAnyGetter
    public Map<String, String> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, String value) {
        this.additionalProperties.put(name, value);
    }
}
