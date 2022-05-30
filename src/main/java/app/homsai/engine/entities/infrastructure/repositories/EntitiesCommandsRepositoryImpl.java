package app.homsai.engine.entities.infrastructure.repositories;

import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.repositories.EntitiesCommandsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Repository
public class EntitiesCommandsRepositoryImpl implements EntitiesCommandsRepository {

    @Autowired
    HAEntityCommandsJpaRepository haEntityCommandsJpaRepository;

    @Autowired
    AreaCommandsJpaRepository areaCommandsJpaRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @Override
    public Area saveArea(Area area) {
        return areaCommandsJpaRepository.save(area);
    }


    @Override
    public HAEntity saveHAEntity(HAEntity haEntity) {
        return haEntityCommandsJpaRepository.save(haEntity);
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

}
