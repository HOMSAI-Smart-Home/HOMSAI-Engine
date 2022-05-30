package app.homsai.engine.homeassistant.gateways.dto.ws;

public class HomeAssistantWSSearchRelatedRequestDto {

    private String itemType = "entity";

    private String type = "search/related";

    private String itemId;

    private int id;

    public HomeAssistantWSSearchRelatedRequestDto(String entityId, int id) {
        this.id = id;
        this.itemId = entityId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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
