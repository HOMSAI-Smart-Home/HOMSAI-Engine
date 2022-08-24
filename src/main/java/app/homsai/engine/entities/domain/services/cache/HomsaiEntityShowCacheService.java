package app.homsai.engine.entities.domain.services.cache;

import app.homsai.engine.entities.application.http.dtos.HomsaiEntityShowDto;

import java.util.List;

public interface HomsaiEntityShowCacheService {
    List<HomsaiEntityShowDto> getHomsaiEntityShowDtoList();

    void setHomsaiEntityShowDtoList(List<HomsaiEntityShowDto> homsaiEntityShowDtoList);
}
