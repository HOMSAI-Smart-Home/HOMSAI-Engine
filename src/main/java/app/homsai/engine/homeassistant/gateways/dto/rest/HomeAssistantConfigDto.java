
package app.homsai.engine.homeassistant.gateways.dto.rest;

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
    "latitude",
    "longitude",
    "elevation",
    "unit_system",
    "location_name",
    "time_zone",
    "allowlist_external_urls",
    "version",
    "config_source",
    "safe_mode",
    "state",
    "external_url",
    "internal_url",
    "currency"
})
@Generated("jsonschema2pojo")
public class HomeAssistantConfigDto {

    @JsonProperty("latitude")
    private Double latitude;
    @JsonProperty("longitude")
    private Double longitude;
    @JsonProperty("elevation")
    private Integer elevation;
    @JsonProperty("unit_system")
    private HomeAssistantUnitSystemDto homeAssistantUnitSystemDto;
    @JsonProperty("location_name")
    private String locationName;
    @JsonProperty("time_zone")
    private String timeZone;
    @JsonProperty("allowlist_external_urls")
    private List<Object> allowlistExternalUrls = null;
    @JsonProperty("version")
    private String version;
    @JsonProperty("config_source")
    private String configSource;
    @JsonProperty("safe_mode")
    private Boolean safeMode;
    @JsonProperty("state")
    private String state;
    @JsonProperty("external_url")
    private String externalUrl;
    @JsonProperty("internal_url")
    private String internalUrl;
    @JsonProperty("currency")
    private String currency;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("latitude")
    public Double getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public Double getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("elevation")
    public Integer getElevation() {
        return elevation;
    }

    @JsonProperty("elevation")
    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    @JsonProperty("unit_system")
    public HomeAssistantUnitSystemDto getUnitSystem() {
        return homeAssistantUnitSystemDto;
    }

    @JsonProperty("unit_system")
    public void setUnitSystem(HomeAssistantUnitSystemDto homeAssistantUnitSystemDto) {
        this.homeAssistantUnitSystemDto = homeAssistantUnitSystemDto;
    }

    @JsonProperty("location_name")
    public String getLocationName() {
        return locationName;
    }

    @JsonProperty("location_name")
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @JsonProperty("time_zone")
    public String getTimeZone() {
        return timeZone;
    }

    @JsonProperty("time_zone")
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @JsonProperty("allowlist_external_urls")
    public List<Object> getAllowlistExternalUrls() {
        return allowlistExternalUrls;
    }

    @JsonProperty("allowlist_external_urls")
    public void setAllowlistExternalUrls(List<Object> allowlistExternalUrls) {
        this.allowlistExternalUrls = allowlistExternalUrls;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("config_source")
    public String getConfigSource() {
        return configSource;
    }

    @JsonProperty("config_source")
    public void setConfigSource(String configSource) {
        this.configSource = configSource;
    }

    @JsonProperty("safe_mode")
    public Boolean getSafeMode() {
        return safeMode;
    }

    @JsonProperty("safe_mode")
    public void setSafeMode(Boolean safeMode) {
        this.safeMode = safeMode;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("external_url")
    public String getExternalUrl() {
        return externalUrl;
    }

    @JsonProperty("external_url")
    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    @JsonProperty("internal_url")
    public String getInternalUrl() {
        return internalUrl;
    }

    @JsonProperty("internal_url")
    public void setInternalUrl(String internalUrl) {
        this.internalUrl = internalUrl;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
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
