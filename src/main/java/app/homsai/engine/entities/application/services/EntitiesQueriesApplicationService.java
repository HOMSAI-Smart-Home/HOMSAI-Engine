package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.dtos.HAEntityDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntitiesQueriesApplicationService {

    Page<HAEntityDto> getAllHomeAssistantEntities(Pageable pageRequest, String search);

    Page<HomsaiEntityDto> getAllHomsaiEntities(Pageable pageRequest, String search);
}
