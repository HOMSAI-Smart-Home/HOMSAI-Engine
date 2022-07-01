
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
    "condition",
    "precipitation",
    "temperature",
    "templow",
    "datetime",
    "wind_bearing",
    "wind_speed"
})
@Generated("jsonschema2pojo")
public class HomeAssistantForecastDto {

    @JsonProperty("condition")
    private String condition;
    @JsonProperty("precipitation")
    private String precipitation;
    @JsonProperty("temperature")
    private String temperature;
    @JsonProperty("templow")
    private String templow;
    @JsonProperty("datetime")
    private String datetime;
    @JsonProperty("wind_bearing")
    private String windBearing;
    @JsonProperty("wind_speed")
    private String windSpeed;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("condition")
    public String getCondition() {
        return condition;
    }

    @JsonProperty("condition")
    public void setCondition(String condition) {
        this.condition = condition;
    }

    @JsonProperty("precipitation")
    public String getPrecipitation() {
        return precipitation;
    }

    @JsonProperty("precipitation")
    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }

    @JsonProperty("temperature")
    public String getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature")
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("templow")
    public String getTemplow() {
        return templow;
    }

    @JsonProperty("templow")
    public void setTemplow(String templow) {
        this.templow = templow;
    }

    @JsonProperty("datetime")
    public String getDatetime() {
        return datetime;
    }

    @JsonProperty("datetime")
    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @JsonProperty("wind_bearing")
    public String getWindBearing() {
        return windBearing;
    }

    @JsonProperty("wind_bearing")
    public void setWindBearing(String windBearing) {
        this.windBearing = windBearing;
    }

    @JsonProperty("wind_speed")
    public String getWindSpeed() {
        return windSpeed;
    }

    @JsonProperty("wind_speed")
    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
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
