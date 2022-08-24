package app.homsai.engine.pvoptimizer.domain.services.cache;

import app.homsai.engine.pvoptimizer.application.http.dtos.HvacOptimizerDeviceInitializationCacheDto;
import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;

public interface HomsaiOptimizerHVACDeviceInitializationCacheService {

    HvacOptimizerDeviceInitializationCacheDto getHvacDeviceCacheDto();

    void startHvacDeviceInit(Integer totalTimeSeconds);

    void onProgress(Integer elapsedTimeDelta, String log, HVACDevice syncedDevice);

    void endHvacDeviceInit();
}
