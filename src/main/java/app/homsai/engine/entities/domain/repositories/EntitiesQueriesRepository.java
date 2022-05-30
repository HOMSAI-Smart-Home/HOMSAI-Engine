package app.homsai.engine.entities.domain.repositories;

import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.exceptions.HAEntityNotFoundException;
import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HAEntity;
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
}
