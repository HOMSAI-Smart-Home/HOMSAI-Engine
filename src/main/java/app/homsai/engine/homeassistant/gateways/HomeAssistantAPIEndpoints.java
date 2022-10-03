package app.homsai.engine.homeassistant.gateways;

public class HomeAssistantAPIEndpoints {

    public static final String GET_ENTITIES_LIST = "/api/states";

    public static final String GET_CONFIG = "/api/config";

    public static final String SET_ENTITY_STATE = "/api/services/{context}/{command}";

    public static final String GET_ENTITY_STATE = "/api/states/{entity_id}";

    public static final String GET_HISTORY_STATES = "/api/history/period/{start_datetime}";

}
