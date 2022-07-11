package app.homsai.engine.entities.application.http.converters;


import app.homsai.engine.entities.application.http.dtos.*;
import app.homsai.engine.entities.domain.models.*;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.optimizations.application.http.dtos.HvacDeviceDto;
import app.homsai.engine.optimizations.domain.models.HvacDevice;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

/**
 * Created by Giacomo Agostini on 04/04/22.
 */

public interface EntitiesMapper {


    List<HAEntity> convertFromDto(List<HomeAssistantEntityDto> homeAssistantEntityDtoList);

    List<HAEntityDto> convertToDto(Page<HAEntity> haEntities);

    List<HomsaiEntityDto> convertToHomsaiDto(Page<HomsaiEntity> homsaiEntities);

    HomsaiEntitiesHistoricalStateDto convertToDto(HomsaiEntitiesHistoricalState saveHomsaiEntityHistoricalState);

    List<HomsaiEntitiesHistoricalStateDto> convertHistoricalListToDto(List<HomsaiEntitiesHistoricalState> homsaiEntitiesHistoricalStateList);

    List<HomsaiEntitiesHistoricalStateLightDto> convertHistoricalListToLightDto(List<HomsaiEntitiesHistoricalState> content);

    List<HVACDeviceDto> convertToDto(List<HVACDevice> hvacDeviceList);

    HVACDeviceDto convertToDto(HVACDevice syncedDevice);

    List<AreaDto> convertToDtoArea(List<Area> allAreas);

    HvacDeviceDto convertToDto(HvacDevice hvacDevice);

    List<HvacDeviceDto> convertToDto(Collection<HvacDevice> values);
}
