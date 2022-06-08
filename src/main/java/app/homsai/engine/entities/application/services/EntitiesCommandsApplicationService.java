package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.dtos.HVACDeviceDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntitiesHistoricalStateDto;
import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;

import java.util.List;

public interface EntitiesCommandsApplicationService {

    void syncHomeAssistantEntities() throws InterruptedException;

    List<HomsaiEntitiesHistoricalStateDto> syncHomsaiEntitiesValues() throws AreaNotFoundException;

    void addExcludedHAEntities(List<String> excludedIds) throws InterruptedException;

    List<HVACDeviceDto> initHVACDevices(Integer type) throws InterruptedException;
}
