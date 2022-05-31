package app.homsai.engine.entities.domain.repositories;

import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.exceptions.HAEntityNotFoundException;
import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.models.HomsaiEntity;

import java.util.List;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */


public interface EntitiesCommandsRepository {
    Area saveArea(Area area);

    HAEntity saveHAEntity(HAEntity haEntity);

    List<HAEntity> saveAllHAEntities(List<HAEntity> haEntityList);

    void truncateAreas();

    void truncateHAEntities();

    void truncateHomsaiEntities();

    HomsaiEntity saveHomsaiEntity(HomsaiEntity homsaiEntity);
}
