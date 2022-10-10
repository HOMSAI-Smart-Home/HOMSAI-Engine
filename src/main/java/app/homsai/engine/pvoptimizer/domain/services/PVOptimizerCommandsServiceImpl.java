package app.homsai.engine.pvoptimizer.domain.services;

import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.entities.domain.models.SampledSignal;
import app.homsai.engine.entities.domain.services.EntitiesQueriesService;
import app.homsai.engine.homeassistant.application.services.HomeAssistantCommandsApplicationService;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import app.homsai.engine.pvoptimizer.domain.repositories.PVOptimizerCommandsRepository;
import app.homsai.engine.pvoptimizer.domain.services.cache.HomsaiOptimizerHVACDeviceInitializationCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static app.homsai.engine.common.domain.utils.Consts.*;

@Service
public class PVOptimizerCommandsServiceImpl implements PVOptimizerCommandsService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HomsaiOptimizerHVACDeviceInitializationCacheService homsaiOptimizerHVACDeviceInitializationCacheService;

    @Autowired
    EntitiesQueriesService entitiesQueriesService;

    @Autowired
    HomeAssistantCommandsApplicationService homeAssistantCommandsApplicationService;

    @Autowired
    HomeAssistantQueriesApplicationService homeAssistantQueriesApplicationService;

    @Autowired
    PVOptimizerCommandsRepository pvOptimizerCommandsRepository;

    @Override
    @Async("threadPoolTaskExecutor")
    public void initHomsaiHvacDevices(List<HVACDevice> hvacDeviceList, Integer type, String hvacFunction) throws InterruptedException, HvacPowerMeterIdNotSet {
        homsaiOptimizerHVACDeviceInitializationCacheService.startHvacDeviceInit(calculateInitTime(hvacDeviceList.size()).intValue(), type);
        Double baseConsumption = readBaseConsumption(hvacDeviceList, type);
        logger.info("average base consumption: "+baseConsumption);
        homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, "average base consumption: "+baseConsumption, null);

        Double nextHvacDeviceSetTemp = null;
        for(HVACDevice hvacDevice : hvacDeviceList){
            Double hvacGrossDeviceConsumption = 0D;
            Double hvacGrossCoupledDeviceConsumption = 0D;
            try {
                HashMap<String, Double> hvacDeviceInitInfo =
                        readHvacDeviceConsumption(hvacDevice, hvacDeviceList, type, hvacFunction, nextHvacDeviceSetTemp);
                hvacGrossDeviceConsumption = hvacDeviceInitInfo.get(INIT_HVAC_DEVICE_COMSUMPTION);
                hvacGrossCoupledDeviceConsumption = hvacDeviceInitInfo.get(INIT_COUPLED_HVAC_DEVICE_COMSUMPTION);
                nextHvacDeviceSetTemp = hvacDeviceInitInfo.get(INIT_NEXT_HVAC_DEVICE_OLD_SET_TEMP);
            }catch (Exception e){
                e.printStackTrace();
                homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(HVAC_INITIALIZATION_DURATION_MINUTES*60, hvacDevice.getEntityId()+": error on communication", null);
            }
            Double hvacNetDeviceConsumption = hvacGrossDeviceConsumption - baseConsumption;
            logger.info(hvacDevice.getEntityId()+": average gross climate consumption: "+hvacGrossDeviceConsumption);
            homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, "average gross consumption: "+hvacGrossDeviceConsumption, null);
            logger.info(hvacDevice.getEntityId()+": average net climate device consumption: "+hvacNetDeviceConsumption);
            homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, "average net consumption: "+hvacNetDeviceConsumption, null);
            if(hvacNetDeviceConsumption >= HVAC_INITIALIZATION_WATT_THRESHOLD){
                hvacDevice.setPowerConsumption(hvacNetDeviceConsumption);
                //TODO: setCoupledPowerConsumption
                pvOptimizerCommandsRepository.saveHvacDevice(hvacDevice);
                logger.info(hvacDevice.getEntityId()+": successfully saved");
                homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, hvacDevice.getEntityId()+": successfully saved", hvacDevice);
            } else {
                logger.info(hvacDevice.getEntityId()+": under threshold, discarded");
                homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, hvacDevice.getEntityId()+": under threshold, discarded", null);
            }
        }
        homsaiOptimizerHVACDeviceInitializationCacheService.endHvacDeviceInit();
    }


    private Double readBaseConsumption(List<HVACDevice> hvacDeviceList, Integer type) throws InterruptedException, HvacPowerMeterIdNotSet {
        HomeInfo homeInfo = entitiesQueriesService.findHomeInfo();
        String meterEntityId = type == PV_OPTIMIZATION_MODE_WINTER ? homeInfo.getHvacWinterPowerMeterId() : homeInfo.getHvacSummerPowerMeterId();
        SampledSignal baseConsumption = new SampledSignal();
        if(meterEntityId == null)
            throw new HvacPowerMeterIdNotSet();
        for(HVACDevice hvacDevice : hvacDeviceList){
            homeAssistantCommandsApplicationService.sendHomeAssistantClimateHVACMode(hvacDevice.getEntityId(), Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION);
            logger.info("sent cmd off to: "+hvacDevice.getEntityId());
            homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, "sent cmd off to: "+hvacDevice.getEntityId(), null);
        }
        logger.info("waiting 30s to ensure devices turn off");
        homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000, "waiting 30s to ensure devices turn off", null);
        Thread.sleep(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS);
        for(int i = 0; i < calcInitBaseConsumptionCycles(); i++){
            HomeAssistantEntityDto climatePowerEntityDto = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(meterEntityId);
            Double value = HOME_ASSISTANT_WATT.equals(climatePowerEntityDto.getAttributes().getUnitOfMeasurement())? Double.parseDouble(climatePowerEntityDto.getState()) : Double.parseDouble(climatePowerEntityDto.getState()) * 1000;
            baseConsumption.addEntry(value);
            logger.info("base consumption: "+value);
            homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000, "base consumption: "+value, null);
            Thread.sleep(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS);
        }
        return baseConsumption.getAverage();
    }

    private HashMap<String, Double> readHvacDeviceConsumption(
            HVACDevice hvacDevice,
            List<HVACDevice> hvacDeviceList,
            Integer type,
            String hvacMode,
            Double oldSetTempFromPreviousIteraction) throws InterruptedException, HvacPowerMeterIdNotSet {
        HashMap<String, Double> map = new HashMap<>();
        SampledSignal climateConsumption = new SampledSignal();
        SampledSignal coupledClimateConsumption = new SampledSignal();

        HomeInfo homeInfo = entitiesQueriesService.findHomeInfo();
        String meterEntityId = type == PV_OPTIMIZATION_MODE_WINTER ? homeInfo.getHvacWinterPowerMeterId() : homeInfo.getHvacSummerPowerMeterId();
        if(meterEntityId == null)
            throw new HvacPowerMeterIdNotSet();

        boolean nextHvacDeviceStarted = false;

        String climateEntityId = hvacDevice.getEntityId();
        Double oldSetTemp = oldSetTempFromPreviousIteraction != null ? oldSetTempFromPreviousIteraction : getSetTemp(climateEntityId);

        Double nextOldSetTemp = null;
        String nextClimateEntityId = null;

        if (oldSetTempFromPreviousIteraction == null) {
            sendHomeAssistantInitCommads(hvacDevice, climateEntityId, type, hvacMode);
            homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000, "sent cmd cooling to: " + climateEntityId + '\n' + "waiting 30s to init", null);
        }

        for(int i = 0; i < calcInitHvacDeviceCycles(); i++){
            HomeAssistantEntityDto climatePowerEntityDto = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(meterEntityId);
            Double value = HOME_ASSISTANT_WATT.equals(climatePowerEntityDto.getAttributes().getUnitOfMeasurement())? Double.parseDouble(climatePowerEntityDto.getState()) : Double.parseDouble(climatePowerEntityDto.getState()) * 1000;
            logger.info("climate consumption: "+value);
            homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000, "climate gross consumption: "+value, null);

            boolean isNextHvacDeviceStartingPhase = false;

            if (i < calcHvacDeviceCyclesForNextInit()) {
                climateConsumption.addEntry(value);
            } else {
                if (hvacDeviceList.size() > 1 && !nextHvacDeviceStarted) {
                    climateConsumption.addEntry(value);

                    int currentHvacDeviceIndex = hvacDeviceList.indexOf(hvacDevice);
                    HVACDevice nextHvacDevice =
                            currentHvacDeviceIndex == hvacDeviceList.size() - 1 ? hvacDeviceList.get(0) : hvacDeviceList.get(currentHvacDeviceIndex + 1);
                    nextClimateEntityId = nextHvacDevice.getEntityId();
                    nextOldSetTemp = getSetTemp(climateEntityId);
                    sendHomeAssistantInitCommads(nextHvacDevice, nextClimateEntityId, type, hvacMode);
                    isNextHvacDeviceStartingPhase = true;
                    nextHvacDeviceStarted = true;
                } else {
                    coupledClimateConsumption.addEntry(value);
                }
            }

            if (!isNextHvacDeviceStartingPhase) {
                Thread.sleep(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS);
            }
        }

        sendHomeAssistantOffCommands(climateEntityId, oldSetTemp);

        if (hvacDeviceList.indexOf(hvacDevice) == hvacDeviceList.size() -1) {
            sendHomeAssistantOffCommands(nextClimateEntityId, nextOldSetTemp);
        }

        map.put(INIT_HVAC_DEVICE_COMSUMPTION, climateConsumption.getAverage());
        map.put(INIT_COUPLED_HVAC_DEVICE_COMSUMPTION, coupledClimateConsumption.getAverage());
        map.put(INIT_NEXT_HVAC_DEVICE_OLD_SET_TEMP, nextOldSetTemp);

        return map;
    }

    private void sendHomeAssistantInitCommads(
            HVACDevice hvacDevice,
            String climateEntityId,
            Integer type,
            String hvacMode) throws InterruptedException {
        Double initSetTemp = type == HVAC_MODE_WINTER_ID ? hvacDevice.getMaxTemp() : hvacDevice.getMinTemp();
        homeAssistantCommandsApplicationService.sendHomeAssistantClimateTemperature(climateEntityId, initSetTemp);
        logger.info("sent "+initSetTemp+"° to: "+climateEntityId);
        homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, "sent "+initSetTemp+"° to: "+climateEntityId, null);
        homeAssistantCommandsApplicationService.sendHomeAssistantClimateHVACMode(climateEntityId, hvacMode);
        logger.info("sent cmd cooling to: "+climateEntityId);
        logger.info("waiting 30s to init");
        Thread.sleep(HVAC_INITIALIZATION_SLEEP_TIME_MILLIS);
    }

    private void sendHomeAssistantOffCommands(String climateEntityId, Double oldSetTemp) {
        homeAssistantCommandsApplicationService.sendHomeAssistantClimateHVACMode(climateEntityId, Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION);
        logger.info("sent "+oldSetTemp+"° to: "+climateEntityId);
        homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, "sent "+oldSetTemp+"° to: "+climateEntityId, null);
        homeAssistantCommandsApplicationService.sendHomeAssistantClimateTemperature(climateEntityId, oldSetTemp);
        logger.info("finish "+climateEntityId);
        homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, "finish "+climateEntityId, null);
    }

    private int calcInitHvacDeviceCycles(){
        return  HVAC_INITIALIZATION_DURATION_MINUTES * 60 / (HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000);
    }

    private int calcHvacDeviceCyclesForNextInit(){
        return calcInitHvacDeviceCycles() / 2;
    }

    private int calcInitBaseConsumptionCycles(){
        return  HVAC_BC_INITIALIZATION_DURATION_MINUTES * 60 / (HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000);
    }

    private double getSetTemp(String entityId){
        try {
            return Double.parseDouble(homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(entityId).getAttributes().getTemperature());
        } catch (Exception e){
            e.printStackTrace();
            return 24D;
        }
    }

    @Override
    public Double calculateInitTime(Integer deviceSize) {
        return (HVAC_BC_INITIALIZATION_DURATION_MINUTES.doubleValue() * 60D + HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000) +
                deviceSize * (HVAC_INITIALIZATION_DURATION_MINUTES.doubleValue() * 60D + HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000)
                + HVAC_INITIALIZATION_INFRA_TIME_DURATION_MILLIS / 1000 * (deviceSize-1);
    }

    @Override
    public void deleteFromHvacDevicesByType(Integer type) {
        pvOptimizerCommandsRepository.deleteFromHvacDevicesByType(type);
    }

    @Override
    public HVACDevice updateHvacDevice(HVACDevice hvacDevice) {
        return pvOptimizerCommandsRepository.updateHvacDevice(hvacDevice);
    }


}
