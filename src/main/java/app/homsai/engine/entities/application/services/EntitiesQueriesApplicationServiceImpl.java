package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.converters.EntitiesMapper;
import app.homsai.engine.entities.application.http.dtos.*;
import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.models.HomsaiEntitiesHistoricalState;
import app.homsai.engine.entities.domain.models.HomsaiEntity;
import app.homsai.engine.entities.domain.services.EntitiesQueriesService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    @Override
    @Transactional
    public List<HomsaiEntityShowDto> getAllLastHomsaiEntityToShow(){
        List<HomsaiEntityShowDto> homsaiEntityShowDtos = new ArrayList<>();
        List<Area> areaDtoList = entitiesQueriesService.findAllAreas();
        for(Area area : areaDtoList){
            String search = "area:"+area.getName();
            List<HomsaiEntitiesHistoricalState> homsaiEntitiesHistoricalStates = entitiesQueriesService.findAllHomsaiHistoricalStates(PageRequest.of(0, 2, Sort.by("timestamp").descending()), search).getContent();
            if(homsaiEntitiesHistoricalStates.size() > 0){
                HomsaiEntityShowDto homsaiEntityShowDto = new HomsaiEntityShowDto();
                homsaiEntityShowDto.setArea(area.getName());
                for(HomsaiEntitiesHistoricalState homsaiEntitiesHistoricalState : Lists.reverse(homsaiEntitiesHistoricalStates)){
                    if("temperature".equals(homsaiEntitiesHistoricalState.getType().getDeviceClass())){
                        homsaiEntityShowDto.setTemperature(String.format("%.2f",homsaiEntitiesHistoricalState.getValue())+homsaiEntitiesHistoricalState.getUnitOfMeasurement());
                        homsaiEntityShowDto.setTime(ChronoUnit.MINUTES.between(homsaiEntitiesHistoricalState.getTimestamp(), Instant.now())+" minutes ago");
                    }
                    if("humidity".equals(homsaiEntitiesHistoricalState.getType().getDeviceClass())){
                        homsaiEntityShowDto.setHumidity(String.format("%.2f",homsaiEntitiesHistoricalState.getValue())+homsaiEntitiesHistoricalState.getUnitOfMeasurement());
                        homsaiEntityShowDto.setTime(ChronoUnit.MINUTES.between(homsaiEntitiesHistoricalState.getTimestamp(), Instant.now())+" minutes ago");
                    }
                }
                homsaiEntityShowDtos.add(homsaiEntityShowDto);
            }
        }
        return homsaiEntityShowDtos;
    }
}
