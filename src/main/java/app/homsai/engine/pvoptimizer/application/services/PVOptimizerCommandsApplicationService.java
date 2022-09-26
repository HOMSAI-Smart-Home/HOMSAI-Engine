package app.homsai.engine.pvoptimizer.application.services;

import app.homsai.engine.common.domain.exceptions.BadRequestException;
import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.exceptions.BadIntervalsException;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import app.homsai.engine.pvoptimizer.application.http.dtos.*;
import app.homsai.engine.pvoptimizer.domain.exceptions.ClimateEntityNotFoundException;
import app.homsai.engine.pvoptimizer.domain.exceptions.HvacEntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PVOptimizerCommandsApplicationService {

    @Transactional
    HVACDeviceInitDto initHVACDevices(Integer type) throws InterruptedException, HvacPowerMeterIdNotSet, ClimateEntityNotFoundException;

    Integer getHvacDeviceInitTimeSeconds(Integer type);

    HvacDeviceSettingDto updateHvacDeviceSetting(String hvacDeviceEntityId, HvacDeviceSettingDto hvacDeviceSettingDto) throws BadIntervalsException;

    HomeHvacSettingsDto updateHomeHvacSettings(HomeHvacSettingsUpdateDto homeHvacSettingsUpdateDto) throws BadRequestException, BadHomeInfoException;
}
