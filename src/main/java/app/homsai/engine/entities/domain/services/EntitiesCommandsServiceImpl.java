package app.homsai.engine.entities.domain.services;

import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.repositories.EntitiesCommandsRepository;
import app.homsai.engine.entities.domain.repositories.EntitiesQueriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntitiesCommandsServiceImpl implements EntitiesCommandsService {

    @Autowired
    private EntitiesQueriesRepository entitiesQueriesRepository;

    @Autowired
    private EntitiesCommandsRepository entitiesCommandsRepository;

    @Override
    public Area getAreaByNameOrCreate(String name){
        Area area = entitiesQueriesRepository.findOneAreaByName(name);
        if(area == null){
            area = new Area();
            area.setName(name);
            area = entitiesCommandsRepository.saveArea(area);
        }
        return area;
    }

    @Override
    public HAEntity updateHAEntity(HAEntity haEntity){
        return entitiesCommandsRepository.saveHAEntity(haEntity);
    }

    @Override
    public List<HAEntity> saveAllHAEntities(List<HAEntity> haEntityList) {
        return entitiesCommandsRepository.saveAllHAEntities(haEntityList);
    }

    @Override
    public void truncateAreas(){
        entitiesCommandsRepository.truncateAreas();
    }

    @Override
    public void truncateHAEntities(){
        entitiesCommandsRepository.truncateHAEntities();
    }

}
