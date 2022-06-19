package app.homsai.engine.entities.domain.repositories;

import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.exceptions.HAEntityNotFoundException;
import app.homsai.engine.entities.domain.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */


public interface EntitiesQueriesRepository {

    Page<HAEntity> findAllHAEntities(Pageable pageRequest, String search);

    Area findOneArea(String areaUuid) throws AreaNotFoundException;

    Area findOneAreaByName(String name);

    HAEntity findOneHAEntity(String entityUuid) throws HAEntityNotFoundException;

    List<HAEntity> findAllHAEntitiesList();

    List<Area> findAllAreaList();

    List<HomsaiEntityType> findAllHomsaiEntityTypes();

    Page<HomsaiEntity> findAllHomsaiEntities(Pageable pageRequest, String search);

    Page<HomsaiEntitiesHistoricalState> findAllHomsaiHistoricalStates(Pageable pageRequest, String search);

    List<ExcludedHAEntity> findAllExcludedHAEntities();

    HomeInfo getHomeInfo();

    Page<HVACDevice> findAllHvacDevices(Pageable pageable, String search);

    HVACDevice findOneHvacDeviceByEntityId(String entityId);
}
