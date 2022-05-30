
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
    "id",
    "type",
    "success",
    "result"
})
@Generated("jsonschema2pojo")
public class HomeAssistantWSSearchRelatedResponseDto {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("result")
    private HomeAssistantWSSearchRelatedResultResponseDto homeAssistantWSSearchRelatedResultResponseDto;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("success")
    public Boolean getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @JsonProperty("result")
    public HomeAssistantWSSearchRelatedResultResponseDto getResult() {
        return homeAssistantWSSearchRelatedResultResponseDto;
    }

    @JsonProperty("result")
    public void setResult(HomeAssistantWSSearchRelatedResultResponseDto homeAssistantWSSearchRelatedResultResponseDto) {
        this.homeAssistantWSSearchRelatedResultResponseDto = homeAssistantWSSearchRelatedResultResponseDto;
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
