package app.homsai.engine.entities.domain.services;

import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.models.HomsaiEntitiesHistoricalState;
import app.homsai.engine.entities.domain.models.HomsaiEntity;

import java.util.List;

public interface EntitiesCommandsService {

    Area getAreaByNameOrCreate(String name);

    HAEntity updateHAEntity(HAEntity haEntity);

    List<HAEntity> saveAllHAEntities(List<HAEntity> haEntityList);

    void truncateAreas();

    void truncateHAEntities();

    void truncateHomsaiEntities();

    Integer syncHomsaiEntities();

    HomsaiEntitiesHistoricalState saveHomsaiEntityHistoricalState(HomsaiEntitiesHistoricalState homsaiEntitiesHistoricalState);

    List<HomsaiEntitiesHistoricalState> calculateHomsaiEntitiesValues(List<HomsaiEntity> homsaiEntityList);

    List<HomsaiEntitiesHistoricalState> calculateHomsaiHomeValues(List<HomsaiEntitiesHistoricalState> homsaiEntitiesHistoricalStateList) throws AreaNotFoundException;
}
