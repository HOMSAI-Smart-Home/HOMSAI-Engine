package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.dtos.HAEntityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntitiesQueriesApplicationService {

    Page<HAEntityDto> getAllEntities(Pageable pageRequest, String search);
}
