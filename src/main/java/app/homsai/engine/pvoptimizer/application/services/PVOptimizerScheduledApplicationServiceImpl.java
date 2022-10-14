package app.homsai.engine.pvoptimizer.application.services;

import app.homsai.engine.common.domain.utils.constants.Consts;
import app.homsai.engine.pvoptimizer.domain.services.PVOptimizerEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class PVOptimizerScheduledApplicationServiceImpl implements PVOptimizerScheduledApplicationService {

    @Autowired
    PVOptimizerEngineService pvOptimizerEngineService;

    @Scheduled(fixedRate = Consts.HVAC_PV_OPTIMIZATION_UPDATE_TIMEDELTA_MINUTES*60*1000)
    void updateHvacDeviceOptimizationCache(){
        pvOptimizerEngineService.updateHvacDeviceOptimizationCache();
    }

    @Scheduled(fixedRate = Consts.HVAC_PV_OPTIMIZATION_REQUEST_TIMEDELTA_MINUTES*60*1000)
    void updateHvacDeviceOptimizationStatus(){
        pvOptimizerEngineService.updateHvacDeviceOptimizationStatus();
    }

}
