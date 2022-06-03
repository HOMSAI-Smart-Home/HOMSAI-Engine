package app.homsai.engine.entities.domain.services;

import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.models.HomsaiEntitiesHistoricalState;
import app.homsai.engine.entities.domain.models.HomsaiEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntitiesQueriesService {

    Page<HAEntity> findAllEntities(Pageable pageRequest, String search);

    Page<HomsaiEntity> findAllHomsaiEntities(Pageable pageRequest, String search);

    Page<HomsaiEntitiesHistoricalState> findAllHomsaiHistoricalStates(Pageable pageRequest, String search);
}
