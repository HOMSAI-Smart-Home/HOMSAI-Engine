package app.homsai.engine.pvoptimizer.application.services;

import app.homsai.engine.pvoptimizer.application.http.dtos.*;
import app.homsai.engine.pvoptimizer.domain.exceptions.HvacEntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PVOptimizerQueriesApplicationService {


    HvacOptimizerDeviceInitializationCacheDto getHvacInitStatus();

    List<OptimizerHVACDeviceDto> getHvacEntities();

    OptimizerHVACDeviceDto getOneHvacEntity(String entityUuid) throws HvacEntityNotFoundException;

    HomeHvacSettingsDto getHomsaiHvacSettings();

    @Transactional
    List<HVACDeviceDto> getAllHomsaiHvacDevices(Integer hvacDeviceConditioning);

    List<HVACEquipmentDto> getHvacEquipments();

    HvacOptimizerDeviceInitializationEstimatedDto getHvacInitEstimated(Integer type);
}
