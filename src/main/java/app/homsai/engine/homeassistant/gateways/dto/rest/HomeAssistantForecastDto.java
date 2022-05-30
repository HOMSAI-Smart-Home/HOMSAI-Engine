
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
    private Double precipitation;
    @JsonProperty("temperature")
    private Double temperature;
    @JsonProperty("templow")
    private Double templow;
    @JsonProperty("datetime")
    private String datetime;
    @JsonProperty("wind_bearing")
    private Double windBearing;
    @JsonProperty("wind_speed")
    private Double windSpeed;
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
    public Double getPrecipitation() {
        return precipitation;
    }

    @JsonProperty("precipitation")
    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    @JsonProperty("temperature")
    public Double getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature")
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("templow")
    public Double getTemplow() {
        return templow;
    }

    @JsonProperty("templow")
    public void setTemplow(Double templow) {
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
    public Double getWindBearing() {
        return windBearing;
    }

    @JsonProperty("wind_bearing")
    public void setWindBearing(Double windBearing) {
        this.windBearing = windBearing;
    }

    @JsonProperty("wind_speed")
    public Double getWindSpeed() {
        return windSpeed;
    }

    @JsonProperty("wind_speed")
    public void setWindSpeed(Double windSpeed) {
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
