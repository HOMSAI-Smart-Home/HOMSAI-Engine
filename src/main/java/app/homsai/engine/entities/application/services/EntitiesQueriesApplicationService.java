package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EntitiesQueriesApplicationService {

    Page<HAEntityDto> getAllHomeAssistantEntities(Pageable pageRequest, String search);

    Page<HomsaiEntityDto> getAllHomsaiEntities(Pageable pageRequest, String search);

    Page<HomsaiEntitiesHistoricalStateLightDto> getAllHomsaiHistoricalStates(Pageable pageRequest, String search);

    List<HomsaiEntityShowDto> getAllLastHomsaiEntityToShow();

    void cacheAllLastHomsaiEntitiesToShow();
}
