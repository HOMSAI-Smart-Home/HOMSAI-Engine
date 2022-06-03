package app.homsai.engine.entities.infrastructure.repositories;

import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.exceptions.HAEntityNotFoundException;
import app.homsai.engine.entities.domain.models.*;
import app.homsai.engine.entities.domain.repositories.EntitiesQueriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Repository
public class EntitiesQueriesRepositoryImpl implements EntitiesQueriesRepository {

    @Autowired
    HAEntityQueriesJpaRepository haEntityQueriesJpaRepository;

    @Autowired
    AreaQueriesJpaRepository areaQueriesJpaRepository;

    @Autowired
    HomsaiEntityTypeQueriesJpaRepository homsaiEntityTypeQueriesJpaRepository;

    @Autowired
    HomsaiEntityQueriesJpaRepository homsaiEntityQueriesJpaRepository;

    @Autowired
    HomsaiEntityHistoricalStateQueriesJpaRepository homsaiEntityHistoricalStateQueriesJpaRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public Page<HAEntity> findAllHAEntities(Pageable pageRequest, String search){
        return haEntityQueriesJpaRepository.findAllActive(pageRequest, search);
    }


    @Override
    public Area findOneArea(String areaUuid) throws AreaNotFoundException {
        Area area = areaQueriesJpaRepository.findOneActive(areaUuid);
        if (area == null) {
            throw new AreaNotFoundException(areaUuid);
        }
        return area;
    }

    @Override
    public Area findOneAreaByName(String name) {
        return areaQueriesJpaRepository.findOneByNameAndDeletedAtIsNull(name);
    }


    @Override
    public HAEntity findOneHAEntity(String entityUuid) throws HAEntityNotFoundException {
        HAEntity haEntity = haEntityQueriesJpaRepository.findOneActive(entityUuid);
        if (haEntity == null) {
            throw new HAEntityNotFoundException(entityUuid);
        }
        return haEntity;
    }

    @Override
    public List<HAEntity> findAllHAEntitiesList() {
        List<HAEntity> haEntityList = new ArrayList<>();
        haEntityQueriesJpaRepository.findAllActive().forEach(haEntityList::add);
        return haEntityList;
    }

    @Override
    public List<Area> findAllAreaList() {
        List<Area> areaList = new ArrayList<>();
        areaQueriesJpaRepository.findAllActive().forEach(areaList::add);
        return areaList;
    }

    @Override
    public List<HomsaiEntityType> findAllHomsaiEntityTypes() {
        List<HomsaiEntityType> haEntityTypeList = new ArrayList<>();
        homsaiEntityTypeQueriesJpaRepository.findAllActive().forEach(haEntityTypeList::add);
        return haEntityTypeList;
    }

    @Override
    public Page<HomsaiEntity> findAllHomsaiEntities(Pageable pageRequest, String search) {
        return homsaiEntityQueriesJpaRepository.findAllActive(pageRequest, search);
    }

    @Override
    public Page<HomsaiEntitiesHistoricalState> findAllHomsaiHistoricalStates(Pageable pageRequest, String search) {
        return homsaiEntityHistoricalStateQueriesJpaRepository.findAllActive(pageRequest, search);
    }

}
