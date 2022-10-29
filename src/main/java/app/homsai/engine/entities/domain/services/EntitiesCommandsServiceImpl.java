package app.homsai.engine.entities.domain.services;

import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.models.*;
import app.homsai.engine.entities.domain.repositories.EntitiesCommandsRepository;
import app.homsai.engine.entities.domain.repositories.EntitiesQueriesRepository;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static app.homsai.engine.common.domain.utils.constants.Consts.*;
import static app.homsai.engine.common.domain.utils.HomsaiEntityTypesOperators.AVG;

@Service
public class EntitiesCommandsServiceImpl implements EntitiesCommandsService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntitiesQueriesRepository entitiesQueriesRepository;

    @Autowired
    private EntitiesCommandsRepository entitiesCommandsRepository;

    @Autowired
    HomeAssistantQueriesApplicationService homeAssistantQueriesApplicationService;


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

    @Override
    public void truncateHomsaiEntities(){
        entitiesCommandsRepository.truncateHomsaiEntities();
    }

    @Override
    public Integer syncHomsaiEntities() {
        Integer count = 0;
        List<Area> areaList = entitiesQueriesRepository.findAllAreaList();
        List<HomsaiEntityType> homsaiEntityTypes = entitiesQueriesRepository.findAllHomsaiEntityTypes();
        List<String> excludedHAEntityIDs = entitiesQueriesRepository.findAllExcludedHAEntities()
                .stream()
                .map(ExcludedHAEntity::getEntityId)
                .collect(Collectors.toList());
        for(HomsaiEntityType homsaiEntityType : homsaiEntityTypes) {
            for (Area area : areaList) {
                HomsaiEntity homsaiEntity = new HomsaiEntity();
                homsaiEntity.setArea(area);
                homsaiEntity.setType(homsaiEntityType);
                homsaiEntity.setUnitOfMeasurement(homsaiEntityType.getUnitOfMeasurement());
                homsaiEntity.setName(homsaiEntityType.getRootName()+area.getName());
                homsaiEntity.setHaEntities(area.getEntities()
                        .stream()
                        .filter(h -> (homsaiEntityType.getDeviceClass().equals(h.getDeviceClass()) && !excludedHAEntityIDs.contains(h.getEntityId())))
                        .collect(Collectors.toList()));
                if(homsaiEntity.getHaEntities().size() > 0) {
                    entitiesCommandsRepository.saveHomsaiEntity(homsaiEntity);
                    count ++;
                }
            }
        }
        return count;
    }

    @Override
    public HomsaiEntitiesHistoricalState saveHomsaiEntityHistoricalState(HomsaiEntitiesHistoricalState homsaiEntitiesHistoricalState) {
        return entitiesCommandsRepository.saveHomsaiEntityHistoricalState(homsaiEntitiesHistoricalState);
    }

    @Override
    public List<HomsaiEntitiesHistoricalState> calculateHomsaiEntitiesValues(List<HomsaiEntity> homsaiEntityList) {
        List<HomsaiEntitiesHistoricalState> homsaiEntitiesHistoricalStates = new ArrayList<>();
        for(HomsaiEntity homsaiEntity: homsaiEntityList){
            double sum = 0D;
            int count = 0;
            for(HAEntity haEntity : homsaiEntity.getHaEntities()){
                HomeAssistantEntityDto homeAssistantEntityDto = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(haEntity.getEntityId());
                try {
                    sum += Double.parseDouble(homeAssistantEntityDto.getState());
                    count += 1;
                } catch(NumberFormatException pe){
                    sum += 0D;
                }
            }
            double homsaiSensorState;
            switch (homsaiEntity.getType().getOperator()) {
                case AVG:
                default:
                    homsaiSensorState = sum / (double) count;
            }
            HomsaiEntitiesHistoricalState homsaiEntitiesHistoricalState = new HomsaiEntitiesHistoricalState();
            homsaiEntitiesHistoricalState.setArea(homsaiEntity.getArea());
            homsaiEntitiesHistoricalState.setTimestamp(Instant.now());
            homsaiEntitiesHistoricalState.setType(homsaiEntity.getType());
            homsaiEntitiesHistoricalState.setUnitOfMeasurement(homsaiEntity.getUnitOfMeasurement());
            homsaiEntitiesHistoricalState.setValue(homsaiSensorState);
            if(!Double.isNaN(homsaiSensorState))
                homsaiEntitiesHistoricalStates.add(saveHomsaiEntityHistoricalState(homsaiEntitiesHistoricalState));
        }
        return homsaiEntitiesHistoricalStates;
    }

    @Override
    public List<HomsaiEntitiesHistoricalState> calculateHomsaiHomeValues(List<HomsaiEntitiesHistoricalState> homsaiEntitiesHistoricalStateList) throws AreaNotFoundException {
        List<HomsaiEntitiesHistoricalState> homsaiEntitiesHistoricalStates = new ArrayList<>();
        List<String> excludedHAEntityIDs = entitiesQueriesRepository.findAllExcludedHAEntities()
                .stream()
                .map(ExcludedHAEntity::getEntityId)
                .collect(Collectors.toList());
        HashMap<HomsaiEntityType, AverageObject> homsaiHomeEntitiesMap = new HashMap<>();
        Area homeArea = entitiesQueriesRepository.findOneArea(HOME_AREA_UUID);
        for(HomsaiEntitiesHistoricalState homsaiEntitiesHistoricalState : homsaiEntitiesHistoricalStateList){
            if(!excludedHAEntityIDs.contains(homsaiEntitiesHistoricalState.getArea().getName())) {
                homsaiHomeEntitiesMap.computeIfAbsent(homsaiEntitiesHistoricalState.getType(), v -> new AverageObject()).increment(homsaiEntitiesHistoricalState.getValue());
            }
        }
        for(HomsaiEntityType homsaiEntityType : homsaiHomeEntitiesMap.keySet()){
            HomsaiEntitiesHistoricalState homsaiEntitiesHistoricalState = new HomsaiEntitiesHistoricalState();
            homsaiEntitiesHistoricalState.setArea(homeArea);
            homsaiEntitiesHistoricalState.setTimestamp(Instant.now());
            homsaiEntitiesHistoricalState.setType(homsaiEntityType);
            homsaiEntitiesHistoricalState.setUnitOfMeasurement(homsaiEntityType.getUnitOfMeasurement());
            homsaiEntitiesHistoricalState.setValue(homsaiHomeEntitiesMap.get(homsaiEntityType).getAverage());
            if(!Double.isNaN(homsaiEntitiesHistoricalState.getValue()))
                homsaiEntitiesHistoricalStates.add(saveHomsaiEntityHistoricalState(homsaiEntitiesHistoricalState));
        }
        return homsaiEntitiesHistoricalStates;
    }

    @Override
    public ExcludedHAEntity saveExcludedHAEntity(ExcludedHAEntity excludedHAEntity) {
        return entitiesCommandsRepository.saveExcludedHAEntity(excludedHAEntity);
    }



    @Override
    public void updateHomeInfo(HomeInfo homeInfo) throws BadHomeInfoException {
        entitiesCommandsRepository.updateHomeInfo(homeInfo);
    }

    @Override
    public Area updateArea(Area area) {
        return entitiesCommandsRepository.saveArea(area);
    }

    @Override
    public void enableOptimizer(Integer type) throws BadHomeInfoException {
        HomeInfo homeInfo = entitiesQueriesRepository.getHomeInfo();
        homeInfo.setOptimizerMode(type);
        homeInfo.setPvOptimizationsEnabled(true);
        updateHomeInfo(homeInfo);
    }


    private class AverageObject{
        private Double sum;
        private Integer count;

        public AverageObject() {
            sum = 0D;
            count = 0;
        }

        public Double getSum() {
            return sum;
        }

        public void setSum(Double sum) {
            this.sum = sum;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Double getAverage(){
            return this.sum / this.count.doubleValue();
        }

        public void increment(Double value){
            this.sum += value;
            this.count ++;
        }
    }

}
