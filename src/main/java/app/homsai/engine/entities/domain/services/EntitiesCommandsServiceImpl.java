package app.homsai.engine.entities.domain.services;

import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.application.http.cache.HomsaiHVACDeviceCacheRepository;
import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import app.homsai.engine.entities.domain.models.*;
import app.homsai.engine.entities.domain.repositories.EntitiesCommandsRepository;
import app.homsai.engine.entities.domain.repositories.EntitiesQueriesRepository;
import app.homsai.engine.homeassistant.application.services.HomeAssistantCommandsApplicationService;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.HomeAssistantWSAPIGateway;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static app.homsai.engine.common.domain.utils.Consts.*;
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

    @Autowired
    HomeAssistantCommandsApplicationService homeAssistantCommandsApplicationService;

    @Autowired
    HomsaiHVACDeviceCacheRepository homsaiHVACDeviceCacheRepository;


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
    @Async("threadPoolTaskExecutor")
    public void initHomsaiHvacDevices(List<HVACDevice> hvacDeviceList,  String hvacFunction) throws InterruptedException, HvacPowerMeterIdNotSet {

        homsaiHVACDeviceCacheRepository.startHvacDeviceInit(calculateInitTime(hvacDeviceList.size()).intValue());
        //TODO levare sta riga e mettere set mintemp -> restore e dialog
        Collections.reverse(hvacDeviceList);

        Double baseConsumption = readBaseConsumption(hvacDeviceList);
        logger.info("average base consumption: "+baseConsumption);
        homsaiHVACDeviceCacheRepository.onProgress(0, "average base consumption: "+baseConsumption, null);
        int c = 0;
        for(HVACDevice hvacDevice : hvacDeviceList){
            Double hvacGrossDeviceConsumption = readHvacDeviceConsumption(hvacDevice.getEntityId(), hvacFunction);
            Double hvacNetDeviceConsumption = hvacGrossDeviceConsumption - baseConsumption;
            logger.info(hvacDevice.getEntityId()+": average gross climate consumption: "+hvacGrossDeviceConsumption);
            homsaiHVACDeviceCacheRepository.onProgress(0, "average gross consumption: "+hvacGrossDeviceConsumption, null);
            logger.info(hvacDevice.getEntityId()+": average net climate device consumption: "+hvacNetDeviceConsumption);
            homsaiHVACDeviceCacheRepository.onProgress(0, "average net consumption: "+hvacNetDeviceConsumption, null);
            if(hvacNetDeviceConsumption >= HVAC_INITIALIZATION_WATT_THRESHOLD){
                hvacDevice.setPowerConsumption(hvacNetDeviceConsumption);
                entitiesCommandsRepository.saveHvacDevice(hvacDevice);
                logger.info(hvacDevice.getEntityId()+": successfully saved");
                homsaiHVACDeviceCacheRepository.onProgress(0, hvacDevice.getEntityId()+": successfully saved", hvacDevice);
            } else {
                logger.info(hvacDevice.getEntityId()+": under threshold, discarded");
                homsaiHVACDeviceCacheRepository.onProgress(0, hvacDevice.getEntityId()+": under threshold, discarded", null);
            }
            c++;
            if(c<hvacDeviceList.size()) {
                logger.info("waiting 5 minutes until next try");
                homsaiHVACDeviceCacheRepository.onProgress(HVAC_INITIALIZATION_INFRA_TIME_DURATION_MILLIS/1000, "waiting 5 minutes until next try", null);
                Thread.sleep(HVAC_INITIALIZATION_INFRA_TIME_DURATION_MILLIS);
            }
        }
        homsaiHVACDeviceCacheRepository.endHvacDeviceInit();
    }


    private Double readBaseConsumption(List<HVACDevice> hvacDeviceList) throws InterruptedException, HvacPowerMeterIdNotSet {
        String meterEntityId = entitiesQueriesRepository.getHomeInfo().getHvacPowerMeterId();
        SampledSignal baseConsumption = new SampledSignal();
        if(meterEntityId == null)
            throw new HvacPowerMeterIdNotSet();
        for(HVACDevice hvacDevice : hvacDeviceList){
            homeAssistantCommandsApplicationService.sendHomeAssistantClimateHVACMode(hvacDevice.getEntityId(), Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION);
            logger.info("sent cmd off to: "+hvacDevice.getEntityId());
            homsaiHVACDeviceCacheRepository.onProgress(0, "sent cmd off to: "+hvacDevice.getEntityId(), null);
        }
        logger.info("waiting 30s to ensure devices turn off");
        homsaiHVACDeviceCacheRepository.onProgress(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000, "waiting 30s to ensure devices turn off", null);
        Thread.sleep(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS);
        for(int i = 0; i < calcInitBaseConsumptionCycles(); i++){
            HomeAssistantEntityDto climatePowerEntityDto = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(meterEntityId);
            Double value = HOME_ASSISTANT_WATT.equals(climatePowerEntityDto.getAttributes().getUnitOfMeasurement())? Double.parseDouble(climatePowerEntityDto.getState()) : Double.parseDouble(climatePowerEntityDto.getState()) * 1000;
            baseConsumption.addEntry(value);
            logger.info("base consumption: "+value);
            homsaiHVACDeviceCacheRepository.onProgress(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000, "base consumption: "+value, null);
            Thread.sleep(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS);
        }
        return baseConsumption.getAverage();
    }

    private Double readHvacDeviceConsumption(String climateEntityId, String hvacMode) throws InterruptedException, HvacPowerMeterIdNotSet {
        String meterEntityId = entitiesQueriesRepository.getHomeInfo().getHvacPowerMeterId();
        if(meterEntityId == null)
            throw new HvacPowerMeterIdNotSet();
        SampledSignal climateConsumption = new SampledSignal();
        homeAssistantCommandsApplicationService.sendHomeAssistantClimateHVACMode(climateEntityId, hvacMode);
        logger.info("sent cmd cooling to: "+climateEntityId);
        logger.info("waiting 30s to init");
        homsaiHVACDeviceCacheRepository.onProgress(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000, "sent cmd cooling to: "+climateEntityId+'\n'+"waiting 30s to init", null);
        Thread.sleep(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS);
        for(int i = 0; i < calcInitHvacDeviceCycles(); i++){
            HomeAssistantEntityDto climatePowerEntityDto = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(meterEntityId);
            Double value = HOME_ASSISTANT_WATT.equals(climatePowerEntityDto.getAttributes().getUnitOfMeasurement())? Double.parseDouble(climatePowerEntityDto.getState()) : Double.parseDouble(climatePowerEntityDto.getState()) * 1000;
            climateConsumption.addEntry(value);
            logger.info("climate consumption: "+value);
            homsaiHVACDeviceCacheRepository.onProgress(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000, "climate gross consumption: "+value, null);
            Thread.sleep(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS);
        }
        homeAssistantCommandsApplicationService.sendHomeAssistantClimateHVACMode(climateEntityId, Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION);
        logger.info("finish "+climateEntityId);
        homsaiHVACDeviceCacheRepository.onProgress(0, "finish "+climateEntityId, null);
        return climateConsumption.getAverage();
    }

    private int calcInitHvacDeviceCycles(){
        return  HVAC_INITIALIZATION_DURATION_MINUTES * 60 / (HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000);
    }

    private int calcInitBaseConsumptionCycles(){
        return  HVAC_BC_INITIALIZATION_DURATION_MINUTES * 60 / (HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000);
    }

    @Override
    public Double calculateInitTime(Integer deviceSize) {
        return (HVAC_BC_INITIALIZATION_DURATION_MINUTES.doubleValue() * 60D + HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000) +
                deviceSize * (HVAC_INITIALIZATION_DURATION_MINUTES.doubleValue() * 60D + HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000)
                + HVAC_INITIALIZATION_INFRA_TIME_DURATION_MILLIS / 1000 * (deviceSize-1);
    }

    @Override
    public void deleteFromHvacDevicesByType(Integer type) {
        entitiesCommandsRepository.deleteFromHvacDevicesByType(type);
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
