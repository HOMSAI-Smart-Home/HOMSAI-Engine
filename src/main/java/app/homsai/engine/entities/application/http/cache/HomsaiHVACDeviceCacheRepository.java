package app.homsai.engine.entities.application.http.cache;

import app.homsai.engine.entities.application.http.dtos.HomsaiEntityShowDto;
import app.homsai.engine.entities.application.http.dtos.HvacDeviceCacheDto;
import app.homsai.engine.entities.domain.models.HVACDevice;

import java.util.List;

public interface HomsaiHVACDeviceCacheRepository {

    HvacDeviceCacheDto getHvacDeviceCacheDto();

    void startHvacDeviceInit(Integer totalTimeSeconds);

    void onProgress(Integer elapsedTimeDelta, String log, HVACDevice syncedDevice);

    void endHvacDeviceInit();
}
