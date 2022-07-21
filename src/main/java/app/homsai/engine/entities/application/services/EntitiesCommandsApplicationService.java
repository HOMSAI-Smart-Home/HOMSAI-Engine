package app.homsai.engine.entities.application.services;

import app.homsai.engine.common.domain.exceptions.BadRequestException;
import app.homsai.engine.entities.application.http.dtos.HVACDeviceInitDto;
import app.homsai.engine.entities.application.http.dtos.HomeHvacSettingsDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntitiesHistoricalStateDto;
import app.homsai.engine.entities.application.http.dtos.HvacDeviceSettingDto;
import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.exceptions.BadIntervalsException;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import app.homsai.engine.entities.domain.models.HomeInfo;

import java.util.List;

public interface EntitiesCommandsApplicationService {

    void syncHomeAssistantEntities() throws InterruptedException;

    List<HomsaiEntitiesHistoricalStateDto> syncHomsaiEntitiesValues() throws AreaNotFoundException;

    void addExcludedHAEntities(List<String> excludedIds) throws InterruptedException;

    HVACDeviceInitDto initHVACDevices(Integer type) throws InterruptedException, HvacPowerMeterIdNotSet;

    Integer getHvacDeviceInitTimeSeconds();

    void saveHomeInfo(HomeInfo homeInfo);

    HvacDeviceSettingDto updateHvacDeviceSetting(String hvacDeviceEntityId, HvacDeviceSettingDto hvacDeviceSettingDto) throws BadIntervalsException;

    HomeHvacSettingsDto updateHomeHvacSettings(HomeHvacSettingsDto homeHvacSettingsDto) throws BadRequestException, BadHomeInfoException;
}
