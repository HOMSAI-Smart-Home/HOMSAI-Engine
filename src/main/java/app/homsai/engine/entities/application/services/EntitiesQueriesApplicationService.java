package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.dtos.HAEntityDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntitiesHistoricalStateDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntitiesHistoricalStateLightDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntitiesQueriesApplicationService {

    Page<HAEntityDto> getAllHomeAssistantEntities(Pageable pageRequest, String search);

    Page<HomsaiEntityDto> getAllHomsaiEntities(Pageable pageRequest, String search);

    Page<HomsaiEntitiesHistoricalStateLightDto> getAllHomsaiHistoricalStates(Pageable pageRequest, String search);
}
