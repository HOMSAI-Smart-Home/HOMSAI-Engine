package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.dtos.HVACDeviceInitDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntitiesHistoricalStateDto;
import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
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
}
