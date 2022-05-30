package app.homsai.engine.homeassistant.gateways.dto.ws;

public class HomeAssistantWSAuthResponseDto {

    private String type;
    private String ha_version;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHa_version() {
        return ha_version;
    }

    public void setHa_version(String ha_version) {
        this.ha_version = ha_version;
    }
}
