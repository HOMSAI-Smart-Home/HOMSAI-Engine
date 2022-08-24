package app.homsai.engine.pvoptimizer.application.services;

import app.homsai.engine.pvoptimizer.application.http.dtos.HVACDeviceDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HomeHvacSettingsDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HvacOptimizerDeviceInitializationCacheDto;
import app.homsai.engine.pvoptimizer.domain.exceptions.HvacEntityNotFoundException;
import app.homsai.engine.pvoptimizer.application.http.dtos.OptimizerHVACDeviceDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PVOptimizerQueriesApplicationService {


    HvacOptimizerDeviceInitializationCacheDto getHvacInitStatus();

    List<OptimizerHVACDeviceDto> getHvacEntities();

    OptimizerHVACDeviceDto getOneHvacEntity(String entityUuid) throws HvacEntityNotFoundException;

    HomeHvacSettingsDto getHomsaiHvacSettings();

    @Transactional
    List<HVACDeviceDto> getAllHomsaiHvacDevices(Integer hvacDeviceConditioning);
}
