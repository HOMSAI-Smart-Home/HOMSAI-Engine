package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.converters.EntitiesMapper;
import app.homsai.engine.entities.application.http.dtos.HAEntityDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntitiesHistoricalStateDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntitiesHistoricalStateLightDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntityDto;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.models.HomsaiEntitiesHistoricalState;
import app.homsai.engine.entities.domain.models.HomsaiEntity;
import app.homsai.engine.entities.domain.services.EntitiesCommandsService;
import app.homsai.engine.entities.domain.services.EntitiesQueriesService;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.HomeAssistantWSAPIGateway;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntitiesQueriesApplicationServiceImpl implements EntitiesQueriesApplicationService {

    @Autowired
    EntitiesQueriesService entitiesQueriesService;

    @Autowired
    EntitiesMapper entitiesMapper;


    @Override
    public Page<HAEntityDto> getAllHomeAssistantEntities(Pageable pageRequest, String search) {
        Page<HAEntity> haEntities = entitiesQueriesService.findAllEntities(pageRequest, search);
        List<HAEntityDto> haEntityDtos = entitiesMapper.convertToDto(haEntities);
        return new PageImpl<>(haEntityDtos, pageRequest, haEntities.getTotalElements());
    }

    @Override
    public Page<HomsaiEntityDto> getAllHomsaiEntities(Pageable pageRequest, String search) {
        Page<HomsaiEntity> homsaiEntities = entitiesQueriesService.findAllHomsaiEntities(pageRequest, search);
        List<HomsaiEntityDto> homsaiEntityDtos = entitiesMapper.convertToHomsaiDto(homsaiEntities);
        return new PageImpl<>(homsaiEntityDtos, pageRequest, homsaiEntities.getTotalElements());
    }

    @Override
    public Page<HomsaiEntitiesHistoricalStateLightDto> getAllHomsaiHistoricalStates(Pageable pageRequest, String search) {
        Page<HomsaiEntitiesHistoricalState> homsaiEntitiesHistoricalStates = entitiesQueriesService.findAllHomsaiHistoricalStates(pageRequest, search);
        List<HomsaiEntitiesHistoricalStateLightDto> homsaiEntitiesHistoricalStateDtos = entitiesMapper.convertHistoricalListToLightDto(homsaiEntitiesHistoricalStates.getContent());
        return new PageImpl<>(homsaiEntitiesHistoricalStateDtos, pageRequest, homsaiEntitiesHistoricalStates.getTotalElements());
    }
}
