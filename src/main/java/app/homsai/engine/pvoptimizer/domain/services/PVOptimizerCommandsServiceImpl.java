package app.homsai.engine.pvoptimizer.domain.services;

import app.homsai.engine.common.domain.utils.constants.Consts;
import app.homsai.engine.common.domain.utils.constants.ConstsUtils;
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

import java.util.*;

import static app.homsai.engine.common.domain.utils.constants.Consts.*;

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
    
    @Autowired
    ConstsUtils constsUtils;

    @Override
    @Async("threadPoolTaskExecutor")
    public void initHomsaiHvacDevices(List<HVACDevice> hvacDeviceList, Integer type, String hvacFunction) throws InterruptedException, HvacPowerMeterIdNotSet {
        homsaiOptimizerHVACDeviceInitializationCacheService.startHvacDeviceInit(calculateInitTime(hvacDeviceList.size()).intValue(), type);
        Double baseConsumption = readBaseConsumption(hvacDeviceList, type);
        logger.info("average base consumption: {}", baseConsumption);
        homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, "average base consumption: "+baseConsumption, null);

        LinkedHashMap<HVACDevice, HashMap<String, Double>> hvacDeviceInitInfos = new LinkedHashMap<>();
        Double nextHvacDeviceSetTemp = null;
        for(HVACDevice hvacDevice : hvacDeviceList){
            try {
                HashMap<String, Double> hvacDeviceInitInfo =
                        readHvacDeviceConsumption(hvacDevice, hvacDeviceList, type, hvacFunction, nextHvacDeviceSetTemp);
                nextHvacDeviceSetTemp = hvacDeviceInitInfo.get(INIT_NEXT_HVAC_DEVICE_OLD_SET_TEMP);
                hvacDeviceInitInfos.put(hvacDevice, hvacDeviceInitInfo);
            }catch (Exception e){
                e.printStackTrace();
                homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(constsUtils.getHvacInitializationDurationMinutes()*60, hvacDevice.getEntityId()+": error on communication", null);
            }
        }

        for (Map.Entry<HVACDevice, HashMap<String, Double>> hvacDeviceInitInfo: hvacDeviceInitInfos.entrySet()) {
            HVACDevice hvacDevice = hvacDeviceInitInfo.getKey();
            HashMap<String, Double> info = hvacDeviceInitInfo.getValue();

            Double hvacGrossDeviceConsumption = info.get(INIT_HVAC_DEVICE_COMSUMPTION);
            Double hvacGrossCoupledDeviceConsumption =
                    info.get(INIT_COUPLED_HVAC_DEVICE_COMSUMPTION) != 0D ?
                            info.get(INIT_COUPLED_HVAC_DEVICE_COMSUMPTION) -
                                    getNextHvacDevicePowerConsumption(hvacDevice.getEntityId(), hvacDeviceInitInfos) :
                            info.get(INIT_HVAC_DEVICE_COMSUMPTION);

            Double hvacNetDeviceConsumption = handleNetConsumption(hvacDevice, hvacGrossDeviceConsumption, baseConsumption);

            Double hvacNetCoupledDeviceConsumption = handleNetConsumption(hvacDevice, hvacGrossCoupledDeviceConsumption, baseConsumption);

            if(hvacNetDeviceConsumption >= HVAC_INITIALIZATION_WATT_THRESHOLD){
                hvacDevice.setPowerConsumption(hvacNetDeviceConsumption);
                hvacDevice.setCoupledPowerConsumption(hvacNetCoupledDeviceConsumption);
                pvOptimizerCommandsRepository.saveHvacDevice(hvacDevice);
                logger.info("{}: successfully saved", hvacDevice.getEntityId());
                homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, hvacDevice.getEntityId()+": successfully saved", hvacDevice);
            } else {
                logger.info("{}: under threshold, discarded", hvacDevice.getEntityId());
                homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, hvacDevice.getEntityId()+": under threshold, discarded", null);
            }
        }

        homsaiOptimizerHVACDeviceInitializationCacheService.endHvacDeviceInit();
    }

    private Double getNextHvacDevicePowerConsumption(
            String hvacDeviceEntityId, LinkedHashMap<HVACDevice, HashMap<String, Double>> hvacDeviceInitInfos) {
        boolean foundKey = false;
        boolean foundPC = false;
        Double powerConsumption = 0D;
        for (Map.Entry<HVACDevice, HashMap<String, Double>> hvacDeviceInitInfo: hvacDeviceInitInfos.entrySet()) {
            if (foundKey) {
                powerConsumption = hvacDeviceInitInfo.getValue().get(INIT_HVAC_DEVICE_COMSUMPTION);
                foundPC = true;
                break;
            }
            if (hvacDeviceInitInfo.getKey().getEntityId().equals(hvacDeviceEntityId)) {
                foundKey = true;
            }
        }

        if (foundKey && !foundPC) {
            // It means that is the last entity and the method have to return the first device PC
            Iterator<Map.Entry<HVACDevice, HashMap<String, Double>>> iterator =
                    hvacDeviceInitInfos.entrySet().iterator();
            powerConsumption = iterator.next().getValue().get(INIT_HVAC_DEVICE_COMSUMPTION);
        }

        return powerConsumption;
    }

    private Double handleNetConsumption(HVACDevice hvacDevice, Double grossConsumption, Double baseConsumption) {
        Double hvacNetDeviceConsumption = grossConsumption - baseConsumption;
        logger.info("{}: average gross climate consumption: {}", hvacDevice.getEntityId(), grossConsumption);
        homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, "average gross consumption: "+grossConsumption, null);
        logger.info("{}: average net climate device consumption: {}", hvacDevice.getEntityId(), hvacNetDeviceConsumption);
        homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, "average net consumption: "+hvacNetDeviceConsumption, null);
        return hvacNetDeviceConsumption;
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
        homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(constsUtils.getHvacInitializationSleepTimeMillis() / 1000, "waiting 30s to ensure devices turn off", null);
        Thread.sleep(constsUtils.getHvacInitializationSleepTimeMillis());
        for(int i = 0; i < constsUtils.calcInitBaseConsumptionCycles(); i++){
            HomeAssistantEntityDto climatePowerEntityDto = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(meterEntityId);
            Double value = HOME_ASSISTANT_WATT.equals(climatePowerEntityDto.getAttributes().getUnitOfMeasurement())? Double.parseDouble(climatePowerEntityDto.getState()) : Double.parseDouble(climatePowerEntityDto.getState()) * 1000;
            baseConsumption.addEntry(value);
            logger.info("base consumption: "+value);
            homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(constsUtils.getHvacInitializationSleepTimeMillis() / 1000, "base consumption: "+value, null);
            Thread.sleep(constsUtils.getHvacInitializationSleepTimeMillis());
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
            homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(constsUtils.getHvacInitializationSleepTimeMillis() / 1000, "sent cmd cooling to: " + climateEntityId + '\n' + "waiting 30s to init", null);
        }

        for(int i = 0; i < constsUtils.calcInitHvacDeviceCycles(); i++){
            HomeAssistantEntityDto climatePowerEntityDto = homeAssistantQueriesApplicationService.syncHomeAssistantEntityValue(meterEntityId);
            Double value = HOME_ASSISTANT_WATT.equals(climatePowerEntityDto.getAttributes().getUnitOfMeasurement())? Double.parseDouble(climatePowerEntityDto.getState()) : Double.parseDouble(climatePowerEntityDto.getState()) * 1000;
            logger.info("climate consumption: "+value);
            homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(constsUtils.getHvacInitializationSleepTimeMillis() / 1000, "climate gross consumption: "+value, null);

            boolean isNextHvacDeviceStartingPhase = false;

            if (hvacDeviceList.size() == 1 || i < constsUtils.calcHvacDeviceCyclesForNextInit()) {
                climateConsumption.addEntry(value);
            } else if (hvacDeviceList.size() > 1) {
                if (!nextHvacDeviceStarted) {
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
                Thread.sleep(constsUtils.getHvacInitializationSleepTimeMillis());
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
        Thread.sleep(constsUtils.getHvacInitializationSleepTimeMillis());
    }

    private void sendHomeAssistantOffCommands(String climateEntityId, Double oldSetTemp) {
        homeAssistantCommandsApplicationService.sendHomeAssistantClimateHVACMode(climateEntityId, Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION);
        logger.info("sent "+oldSetTemp+"° to: "+climateEntityId);
        homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, "sent "+oldSetTemp+"° to: "+climateEntityId, null);
        homeAssistantCommandsApplicationService.sendHomeAssistantClimateTemperature(climateEntityId, oldSetTemp);
        logger.info("finish "+climateEntityId);
        homsaiOptimizerHVACDeviceInitializationCacheService.onProgress(0, "finish "+climateEntityId, null);
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
        Double readBaseConsumptionSeconds = (constsUtils.calcInitBaseConsumptionCycles() + 1) * constsUtils.getHvacInitializationSleepTimeMillis().doubleValue() / 1000;
        // In readHvacDeviceConsumption:
        // First device: sendInitCommands for itself (sleep) + (sleep * calcInitHvacDeviceCycles)
        // For all others: (sleep * calcInitHvacDeviceCycles)
        Double readHvacDeviceConsumptionSeconds = constsUtils.getHvacInitializationSleepTimeMillis().doubleValue() / 1000 * (1 + constsUtils.calcInitHvacDeviceCycles());

        return readBaseConsumptionSeconds + deviceSize * readHvacDeviceConsumptionSeconds;
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
