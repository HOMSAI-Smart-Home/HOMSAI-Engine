package app.homsai.engine.homeassistant.gateways;

import app.homsai.engine.entities.domain.models.HAEntity;

public interface HomeAssistantWSAPIGateway {

    void syncEntityAreas(HAEntity[] entityArray);

}
