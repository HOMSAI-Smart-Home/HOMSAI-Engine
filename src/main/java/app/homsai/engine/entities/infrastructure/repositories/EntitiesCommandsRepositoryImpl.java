package app.homsai.engine.entities.infrastructure.repositories;

import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.models.*;
import app.homsai.engine.entities.domain.repositories.EntitiesCommandsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static app.homsai.engine.common.domain.utils.Consts.HOME_INFO_UUID;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Repository
public class EntitiesCommandsRepositoryImpl implements EntitiesCommandsRepository {

    @Autowired
    HAEntityCommandsJpaRepository haEntityCommandsJpaRepository;

    @Autowired
    AreaCommandsJpaRepository areaCommandsJpaRepository;

    @Autowired
    HomsaiEntityCommandsJpaRepository homsaiEntityCommandsJpaRepository;

    @Autowired
    HomsaiEntityHistoricalStateCommandsJpaRepository homsaiEntityHistoricalStateCommandsJpaRepository;

    @Autowired
    ExcludedHAEntityCommandsJpaRepository excludedHAEntityCommandsJpaRepository;

    @Autowired
    HVACDeviceCommandsJpaRepository hvacDeviceCommandsJpaRepository;

    @Autowired
    HomeInfoCommandsJpaRepository homeInfoCommandsJpaRepository;


    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @Override
    public Area saveArea(Area area) {
        return areaCommandsJpaRepository.save(area);
    }


    @Override
    public HAEntity saveHAEntity(HAEntity haEntity) {
        return haEntityCommandsJpaRepository.saveAndFlushNow(haEntity);
    }

    @Override
    public List<HAEntity> saveAllHAEntities(List<HAEntity> haEntityList) {
        List<HAEntity> haEntitySavedList = new ArrayList<>();
        haEntityCommandsJpaRepository.saveAll(haEntityList).forEach(haEntitySavedList::add);
        return haEntitySavedList;
    }

    @Override
    public void truncateAreas(){
        areaCommandsJpaRepository.truncate();
    }

    @Override
    public void truncateHAEntities(){
        haEntityCommandsJpaRepository.truncate();
    }

    @Override
    public void truncateHomsaiEntities(){
        homsaiEntityCommandsJpaRepository.truncate();
    }

    @Override
    public HomsaiEntity saveHomsaiEntity(HomsaiEntity homsaiEntity) {
        return homsaiEntityCommandsJpaRepository.save(homsaiEntity);
    }

    @Override
    public HomsaiEntitiesHistoricalState saveHomsaiEntityHistoricalState(HomsaiEntitiesHistoricalState homsaiEntitiesHistoricalState) {
        return homsaiEntityHistoricalStateCommandsJpaRepository.save(homsaiEntitiesHistoricalState);
    }

    @Override
    public ExcludedHAEntity saveExcludedHAEntity(ExcludedHAEntity excludedHAEntity) {
        return excludedHAEntityCommandsJpaRepository.save(excludedHAEntity);
    }

    @Override
    public HomeInfo updateHomeInfo(HomeInfo homeInfo) throws BadHomeInfoException {
        if(!HOME_INFO_UUID.equals(homeInfo.getUuid()))
            throw new BadHomeInfoException();
        return homeInfoCommandsJpaRepository.save(homeInfo);
    }

}
