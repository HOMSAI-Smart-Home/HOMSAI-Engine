package app.homsai.engine.entities.domain.services;

import app.homsai.engine.entities.domain.models.HAEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntitiesQueriesService {

    Page<HAEntity> findAllEntities(Pageable pageRequest, String search);
}
