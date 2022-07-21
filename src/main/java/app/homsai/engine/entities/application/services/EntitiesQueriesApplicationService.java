package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.dtos.*;
import app.homsai.engine.entities.domain.exceptions.HvacEntityNotFoundException;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.optimizations.application.http.dtos.HvacDeviceDto;
import app.homsai.engine.optimizations.domain.models.HvacDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface EntitiesQueriesApplicationService {

    Page<HAEntityDto> getAllHomeAssistantEntities(Pageable pageRequest, String search);

    Page<HomsaiEntityDto> getAllHomsaiEntities(Pageable pageRequest, String search);

    Page<HomsaiEntitiesHistoricalStateLightDto> getAllHomsaiHistoricalStates(Pageable pageRequest, String search);

    List<HomsaiEntityShowDto> getAllLastHomsaiEntityToShow();

    void cacheAllLastHomsaiEntitiesToShow();

    List<HVACDeviceDto> getAllHomsaiHvacDevices(Integer hvacDeviceConditioning);

    List<AreaDto> getAllAreas();

    HomeInfo getHomeInfo();

    HvacDeviceCacheDto getHvacInitStatus();

    List<HvacDeviceDto> getHvacEntities();

    HvacDeviceDto getOneHvacEntity(String entityUuid) throws HvacEntityNotFoundException;

    HomeHvacSettingsDto getHomsaiHvacSettings();
}
