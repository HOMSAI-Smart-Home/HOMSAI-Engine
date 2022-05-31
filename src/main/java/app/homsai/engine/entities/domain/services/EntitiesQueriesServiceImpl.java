package app.homsai.engine.entities.domain.services;

import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.models.HomsaiEntity;
import app.homsai.engine.entities.domain.repositories.EntitiesQueriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EntitiesQueriesServiceImpl implements EntitiesQueriesService {

    @Autowired
    EntitiesQueriesRepository entitiesQueriesRepository;

    @Override
    public Page<HAEntity> findAllEntities(Pageable pageRequest, String search) {
        return entitiesQueriesRepository.findAllHAEntities(pageRequest, search);
    }

    @Override
    public Page<HomsaiEntity> findAllHomsaiEntities(Pageable pageRequest, String search) {
        return entitiesQueriesRepository.findAllHomsaiEntities(pageRequest, search);
    }
}
