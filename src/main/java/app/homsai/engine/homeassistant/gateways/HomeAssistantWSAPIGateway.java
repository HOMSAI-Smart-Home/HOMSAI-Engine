package app.homsai.engine.homeassistant.gateways;

import app.homsai.engine.entities.domain.models.HAEntity;

import java.util.List;

public interface HomeAssistantWSAPIGateway {

    void syncEntityAreas(List<HAEntity> entityList, Object lock);

}
