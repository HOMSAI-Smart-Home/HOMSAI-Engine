package app.homsai.engine.entities.domain.services;

import app.homsai.engine.entities.domain.models.*;
import app.homsai.engine.entities.domain.repositories.EntitiesQueriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EntitiesQueriesServiceImpl implements EntitiesQueriesService {

    @Autowired
    EntitiesQueriesRepository entitiesQueriesRepository;

    @Override
    @Transactional
    public Page<HAEntity> findAllEntities(Pageable pageRequest, String search) {
        return entitiesQueriesRepository.findAllHAEntities(pageRequest, search);
    }

    @Override
    public Page<HomsaiEntity> findAllHomsaiEntities(Pageable pageRequest, String search) {
        return entitiesQueriesRepository.findAllHomsaiEntities(pageRequest, search);
    }

    @Override
    @Transactional
    public Page<HomsaiEntitiesHistoricalState> findAllHomsaiHistoricalStates(Pageable pageRequest, String search) {
        return entitiesQueriesRepository.findAllHomsaiHistoricalStates(pageRequest, search);
    }

    @Override
    public List<Area> findAllAreas() {
        return entitiesQueriesRepository.findAllAreaList();
    }

    @Override
    @Transactional
    public Page<HVACDevice> findAllHomsaiHvacDevices(Pageable pageable, String search) {
        return entitiesQueriesRepository.findAllHvacDevices(pageable, search);
    }

    @Override
    public HomeInfo findHomeInfo() {
        return entitiesQueriesRepository.getHomeInfo();
    }
}
