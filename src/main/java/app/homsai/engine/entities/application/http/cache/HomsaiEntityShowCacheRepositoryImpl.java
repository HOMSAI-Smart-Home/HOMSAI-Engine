package app.homsai.engine.entities.application.http.cache;

import app.homsai.engine.entities.application.http.dtos.HomsaiEntityShowDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomsaiEntityShowCacheRepositoryImpl implements HomsaiEntityShowCacheRepository{

    List<HomsaiEntityShowDto> homsaiEntityShowDtoList = new ArrayList<>();

    @Override
    public List<HomsaiEntityShowDto> getHomsaiEntityShowDtoList() {
        return homsaiEntityShowDtoList;
    }

    @Override
    public void setHomsaiEntityShowDtoList(List<HomsaiEntityShowDto> homsaiEntityShowDtoList) {
        this.homsaiEntityShowDtoList = homsaiEntityShowDtoList;
    }
}
