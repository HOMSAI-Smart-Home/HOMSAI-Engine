package app.homsai.engine.entities.application.http.cache;

import app.homsai.engine.entities.application.http.dtos.HomsaiEntityShowDto;

import java.util.List;

public interface HomsaiEntityShowCacheRepository {
    List<HomsaiEntityShowDto> getHomsaiEntityShowDtoList();

    void setHomsaiEntityShowDtoList(List<HomsaiEntityShowDto> homsaiEntityShowDtoList);
}
