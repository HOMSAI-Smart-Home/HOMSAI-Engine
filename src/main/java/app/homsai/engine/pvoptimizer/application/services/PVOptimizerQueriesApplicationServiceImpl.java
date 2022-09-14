package app.homsai.engine.pvoptimizer.application.services;

import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.pvoptimizer.application.http.dtos.*;
import app.homsai.engine.pvoptimizer.domain.services.PVOptimizerQueriesService;
import app.homsai.engine.pvoptimizer.domain.services.cache.HomsaiOptimizerHVACDeviceInitializationCacheService;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.pvoptimizer.application.http.converters.PVOptimizerMapper;
import app.homsai.engine.pvoptimizer.domain.exceptions.HvacEntityNotFoundException;
import app.homsai.engine.entities.domain.models.*;
import app.homsai.engine.pvoptimizer.domain.services.cache.PVOptimizerCacheService;
import app.homsai.engine.pvoptimizer.domain.models.OptimizerHVACDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PVOptimizerQueriesApplicationServiceImpl implements PVOptimizerQueriesApplicationService {

    @Autowired
    EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    @Autowired
    HomsaiOptimizerHVACDeviceInitializationCacheService homsaiOptimizerHVACDeviceInitializationCacheService;

    @Autowired
    PVOptimizerCacheService pvoptimizerCacheService;

    @Autowired
    PVOptimizerMapper pvOptimizerMapper;

    @Autowired
    PVOptimizerQueriesService pvOptimizerQueriesService;

    @Autowired
    PVOptimizerCommandsApplicationService pvOptimizerCommandsApplicationService;


    @Override
    public HvacOptimizerDeviceInitializationCacheDto getHvacInitStatus() {
        return homsaiOptimizerHVACDeviceInitializationCacheService.getHvacDeviceCacheDto();
    }

    @Override
    public List<OptimizerHVACDeviceDto> getHvacEntities() {
        return pvOptimizerMapper.convertToDto(pvoptimizerCacheService.getHvacDevicesCache().values());
    }

    @Override
    public OptimizerHVACDeviceDto getOneHvacEntity(String entityUuid) throws HvacEntityNotFoundException {
        OptimizerHVACDevice optimizerHVACDevice = pvoptimizerCacheService.getHvacDevicesCache().get(entityUuid);
        if(optimizerHVACDevice == null)
            throw new HvacEntityNotFoundException(entityUuid);
        return pvOptimizerMapper.convertToDto(optimizerHVACDevice);
    }

    @Override
    public HomeHvacSettingsDto getHomsaiHvacSettings() {
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        Area area = entitiesQueriesApplicationService.getHomeArea();
        HomeHvacSettingsDto homeHvacSettingsDto = new HomeHvacSettingsDto();
        homeHvacSettingsDto.setSetTemperature(Consts.HVAC_MODE.equals("summer")?area.getDesiredSummerTemperature():area.getDesiredWinterTemperature());  // ToDo Summer/winter
        homeHvacSettingsDto.setOptimizerEnabled(homeInfo.getPvOptimizationsEnabled());
        return homeHvacSettingsDto;
    }

    @Override
    @Transactional
    public List<HVACDeviceDto> getAllHomsaiHvacDevices(Integer hvacDeviceConditioning) {
        return pvOptimizerMapper.convertToDto(pvOptimizerQueriesService.findAllHomsaiHvacDevices(Pageable.unpaged(), hvacDeviceConditioning == null ? null : "type:"+hvacDeviceConditioning).getContent());
    }

    @Override
    public HvacOptimizerDeviceInitializationEstimatedDto getHvacInitEstimated(Integer type) {
        HvacOptimizerDeviceInitializationEstimatedDto hvacOptimizerDeviceInitializationEstimatedDto = new HvacOptimizerDeviceInitializationEstimatedDto();
        hvacOptimizerDeviceInitializationEstimatedDto.setTotalTimeSeconds(pvOptimizerCommandsApplicationService.getHvacDeviceInitTimeSeconds(type));
        return hvacOptimizerDeviceInitializationEstimatedDto;
    }

}
