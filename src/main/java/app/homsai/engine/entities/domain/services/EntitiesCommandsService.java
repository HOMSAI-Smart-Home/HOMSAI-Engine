package app.homsai.engine.entities.domain.services;

import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HAEntity;

import java.util.List;

public interface EntitiesCommandsService {

    Area getAreaByNameOrCreate(String name);

    HAEntity updateHAEntity(HAEntity haEntity);

    List<HAEntity> saveAllHAEntities(List<HAEntity> haEntityList);

    void truncateAreas();

    void truncateHAEntities();

    void truncateHomsaiEntities();

    void syncHomsaiEntities();
}
