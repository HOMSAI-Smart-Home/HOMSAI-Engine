
package app.homsai.engine.homeassistant.gateways.dto.ws;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "type",
    "success",
    "result"
})
@Generated("jsonschema2pojo")
public class HomeAssistantWSResultResponseDto {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("result")
    private List<HomeAssistantWSResultResultDto> result;
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
    public List<HomeAssistantWSResultResultDto> getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(List<HomeAssistantWSResultResultDto> homeAssistantWSResultResultDtoList) {
        this.result = homeAssistantWSResultResultDtoList;
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
