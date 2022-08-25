package app.homsai.engine.pvoptimizer.application.services;

import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.pvoptimizer.application.http.converters.PVOptimizerMapper;
import app.homsai.engine.pvoptimizer.application.http.dtos.HVACDeviceDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HomeHvacSettingsDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HvacOptimizerDeviceInitializationCacheDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.OptimizerHVACDeviceDto;
import app.homsai.engine.pvoptimizer.domain.exceptions.HvacEntityNotFoundException;
import app.homsai.engine.pvoptimizer.domain.models.OptimizerHVACDevice;
import app.homsai.engine.pvoptimizer.domain.services.PVOptimizerEngineService;
import app.homsai.engine.pvoptimizer.domain.services.PVOptimizerEngineServiceImpl;
import app.homsai.engine.pvoptimizer.domain.services.PVOptimizerQueriesService;
import app.homsai.engine.pvoptimizer.domain.services.cache.HomsaiOptimizerHVACDeviceInitializationCacheService;
import app.homsai.engine.pvoptimizer.domain.services.cache.PVOptimizerCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
