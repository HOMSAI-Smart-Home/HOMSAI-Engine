package app.homsai.engine.entities.application.http.converters;


import app.homsai.engine.entities.application.http.dtos.HAEntityDto;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Giacomo Agostini on 04/04/22.
 */

public interface EntitiesMapper {


    List<HAEntity> convertFromDto(List<HomeAssistantEntityDto> homeAssistantEntityDtoList);

    List<HAEntityDto> convertToDto(Page<HAEntity> haEntities);
}
