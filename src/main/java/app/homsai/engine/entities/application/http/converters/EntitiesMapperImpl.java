package app.homsai.engine.entities.application.http.converters;



import app.homsai.engine.entities.application.http.dtos.*;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.models.HVACDevice;
import app.homsai.engine.entities.domain.models.HomsaiEntitiesHistoricalState;
import app.homsai.engine.entities.domain.models.HomsaiEntity;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Giacomo Agostini on 04/04/22.
 */
@Service
@Transactional
public class EntitiesMapperImpl implements EntitiesMapper {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<HAEntity> convertFromDto(List<HomeAssistantEntityDto> homeAssistantEntityDtoList) {
        return homeAssistantEntityDtoList.stream()
                .map(h -> {
                    if(h.getAttributes() != null && h.getAttributes().getUnitOfMeasurement() != null)
                        return new HAEntity(h.getAttributes().getFriendlyName(), h.getEntityId(), h.getEntityId().split("\\.")[0], h.getAttributes().getUnitOfMeasurement(),  h.getAttributes().getDeviceClass());
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

    @Override
    public List<HomsaiEntityDto> convertToHomsaiDto(Page<HomsaiEntity> homsaiEntities) {
        return homsaiEntities.stream()
                .map(h -> modelMapper.map(h, HomsaiEntityDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public HomsaiEntitiesHistoricalStateDto convertToDto(HomsaiEntitiesHistoricalState homsaiEntityHistoricalState) {
        return modelMapper.map(homsaiEntityHistoricalState, HomsaiEntitiesHistoricalStateDto.class);
    }

    @Override
    public List<HomsaiEntitiesHistoricalStateDto> convertHistoricalListToDto(List<HomsaiEntitiesHistoricalState> homsaiEntitiesHistoricalStateList) {
        return homsaiEntitiesHistoricalStateList.stream()
                .map(h -> modelMapper.map(h, HomsaiEntitiesHistoricalStateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<HomsaiEntitiesHistoricalStateLightDto> convertHistoricalListToLightDto(List<HomsaiEntitiesHistoricalState> homsaiEntitiesHistoricalStateList) {
        return homsaiEntitiesHistoricalStateList.stream()
                .map(h -> modelMapper.map(h, HomsaiEntitiesHistoricalStateLightDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<HVACDeviceDto> convertToDto(List<HVACDevice> hvacDeviceList) {
        return hvacDeviceList.stream()
                .map(h -> modelMapper.map(h, HVACDeviceDto.class))
                .collect(Collectors.toList());
    }
}
