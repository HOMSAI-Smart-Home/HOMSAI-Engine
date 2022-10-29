package app.homsai.engine.entities.domain.services;

import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import app.homsai.engine.entities.domain.models.*;

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

    ExcludedHAEntity saveExcludedHAEntity(ExcludedHAEntity excludedHAEntity);


    void updateHomeInfo(HomeInfo homeInfo) throws BadHomeInfoException;


    Area updateArea(Area area);

    void enableOptimizer(Integer type) throws BadHomeInfoException;
}
