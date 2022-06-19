package app.homsai.engine.entities.domain.repositories;

import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.exceptions.HAEntityNotFoundException;
import app.homsai.engine.entities.domain.models.*;

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

    HomsaiEntitiesHistoricalState saveHomsaiEntityHistoricalState(HomsaiEntitiesHistoricalState homsaiEntitiesHistoricalState);

    ExcludedHAEntity saveExcludedHAEntity(ExcludedHAEntity excludedHAEntity);

    HomeInfo updateHomeInfo(HomeInfo homeInfo) throws BadHomeInfoException;

    HVACDevice saveHvacDevice(HVACDevice hvacDevice);

    void deleteFromHvacDevicesByType(Integer type);

    HVACDevice updateHvacDevice(HVACDevice hvacDevice);
}
