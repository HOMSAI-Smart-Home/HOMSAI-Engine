package app.homsai.engine.entities.application.http.converters;


import app.homsai.engine.entities.application.http.dtos.*;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.models.HVACDevice;
import app.homsai.engine.entities.domain.models.HomsaiEntitiesHistoricalState;
import app.homsai.engine.entities.domain.models.HomsaiEntity;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.springframework.data.domain.Page;

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
}
