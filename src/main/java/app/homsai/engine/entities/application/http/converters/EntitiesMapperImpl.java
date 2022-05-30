package app.homsai.engine.entities.application.http.converters;



import app.homsai.engine.entities.application.http.dtos.HAEntityDto;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Giacomo Agostini on 04/04/22.
 */
@Service
public class EntitiesMapperImpl implements EntitiesMapper {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<HAEntity> convertFromDto(List<HomeAssistantEntityDto> homeAssistantEntityDtoList) {
        return homeAssistantEntityDtoList.stream()
                .map(h -> {
                    if(h.getAttributes() != null && h.getAttributes().getUnitOfMeasurement() != null)
                        return new HAEntity(h.getAttributes().getFriendlyName(), h.getEntityId(), h.getEntityId().split("\\.")[0], h.getAttributes().getUnitOfMeasurement());
                    else
                        return new HAEntity(h.getAttributes().getFriendlyName(), h.getEntityId(), h.getEntityId().split("\\.")[0]);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HAEntityDto> convertToDto(Page<HAEntity> haEntities) {
        return haEntities.stream()
                .map(h -> modelMapper.map(h, HAEntityDto.class))
                .collect(Collectors.toList());
    }
}
