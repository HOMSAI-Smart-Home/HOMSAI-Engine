package app.homsai.engine.homeassistant.gateways.dto.ws;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HomeAssistantWSGenericRequest {

    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private int id;

    public HomeAssistantWSGenericRequest() {
    }

    public HomeAssistantWSGenericRequest(String type, int id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
