package app.homsai.engine.entities.domain.services;

import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
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
    public HomeInfo findHomeInfo() {
        HomeInfo homeInfo = entitiesQueriesRepository.getHomeInfo();
        if (homeInfo.getHvacPowerMeterId() != null) {
            if (homeInfo.getHvacSummerPowerMeterId() == null) {
                homeInfo.setHvacSummerPowerMeterId(homeInfo.getHvacPowerMeterId());
            }
            if (homeInfo.getHvacWinterPowerMeterId() == null) {
                homeInfo.setHvacWinterPowerMeterId(homeInfo.getHvacPowerMeterId());
            }
        }
        return homeInfo;
    }

    @Override
    public Area getHomeArea() {
        try {
            return entitiesQueriesRepository.findOneArea(Consts.HOME_AREA_UUID);
        } catch (AreaNotFoundException e) {
            return null;
        }
    }
}
