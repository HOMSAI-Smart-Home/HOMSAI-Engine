package app.homsai.engine.homeassistant.gateways.dto.ws;

public class HomeAssistantWSAuthRequestDto {

    private String type = "auth";
    private String accessToken;

    public HomeAssistantWSAuthRequestDto(String access_token) {
        this.accessToken = access_token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
