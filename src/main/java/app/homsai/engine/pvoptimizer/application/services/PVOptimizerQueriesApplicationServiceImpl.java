package app.homsai.engine.pvoptimizer.application.services;

import app.homsai.engine.common.domain.utils.constants.Consts;
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
        homeHvacSettingsDto.setSetTemperature(
                homeInfo.getOptimizerMode() == null || Consts.HVAC_MODE_SUMMER_ID == homeInfo.getOptimizerMode() ?
                        area.getDesiredSummerTemperature() : area.getDesiredWinterTemperature()
        );
        homeHvacSettingsDto.setOptimizerEnabled(homeInfo.getPvOptimizationsEnabled());
        homeHvacSettingsDto.setOptimizerMode(homeInfo.getOptimizerMode());
        homeHvacSettingsDto.setHvacSwitchEntityId(homeInfo.getHvacSwitchEntityId());
        if (homeInfo.getCurrentWinterHVACEquipment() != null) {
            homeHvacSettingsDto.setCurrentWinterHVACEquipment(
                    pvOptimizerMapper.convertToDto(homeInfo.getCurrentWinterHVACEquipment())
            );
        }
        if (homeInfo.getCurrentSummerHVACEquipment() != null) {
            homeHvacSettingsDto.setCurrentSummerHVACEquipment(
                    pvOptimizerMapper.convertToDto(homeInfo.getCurrentSummerHVACEquipment())
            );
        }
        return homeHvacSettingsDto;
    }

    @Override
    @Transactional
    public List<HVACDeviceDto> getAllHomsaiHvacDevices(Integer hvacDeviceConditioning) {
        return pvOptimizerMapper.convertToDto(pvOptimizerQueriesService.findAllHomsaiHvacDevices(Pageable.unpaged(), hvacDeviceConditioning == null ? null : "type:"+hvacDeviceConditioning).getContent());
    }

    @Override
    public List<HVACEquipmentDto> getHvacEquipments(String search) {
        return pvOptimizerMapper.convertToDtoEquipments(pvOptimizerQueriesService.findAllHomsaiHvacEquipments(Pageable.unpaged(), search).getContent());
    }
    
    @Override
    public HvacOptimizerDeviceInitializationEstimatedDto getHvacInitEstimated(Integer type) {
        HvacOptimizerDeviceInitializationEstimatedDto hvacOptimizerDeviceInitializationEstimatedDto = new HvacOptimizerDeviceInitializationEstimatedDto();
        hvacOptimizerDeviceInitializationEstimatedDto.setTotalTimeSeconds(pvOptimizerCommandsApplicationService.getHvacDeviceInitTimeSeconds(type));
        return hvacOptimizerDeviceInitializationEstimatedDto;
    }

    @Override
    public List<HVACDeviceDto> getAllHomsaiHvacDevicesFromDB(String search) {
        return pvOptimizerMapper.convertToDto(pvOptimizerQueriesService.findAllHomsaiHvacDevices(Pageable.unpaged(), search).getContent());
    }

    @Override
    public HVACEquipmentDto getHvacEquipment(String equipmentUuid) {
        return pvOptimizerMapper.convertToDto(pvOptimizerQueriesService.findOneHvacEquipment(equipmentUuid));
    }

}
