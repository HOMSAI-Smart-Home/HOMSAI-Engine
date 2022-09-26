package app.homsai.engine.pvoptimizer.domain.services.cache;

import app.homsai.engine.pvoptimizer.application.http.dtos.HvacOptimizerDeviceInitializationCacheDto;
import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import app.homsai.engine.pvoptimizer.application.http.converters.PVOptimizerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class HomsaiOptimizerHVACDeviceInitializationCacheServiceImpl implements HomsaiOptimizerHVACDeviceInitializationCacheService {

    @Autowired
    PVOptimizerMapper pvOptimizerMapper;

    @Autowired
    PVOptimizerCacheService pvoptimizerCacheService;

    private static final HvacOptimizerDeviceInitializationCacheDto hvacOptimizerDeviceInitializationCacheDto = new HvacOptimizerDeviceInitializationCacheDto();

    @Override
    public HvacOptimizerDeviceInitializationCacheDto getHvacDeviceCacheDto() {
        return hvacOptimizerDeviceInitializationCacheDto;
    }

    @Override
    public void startHvacDeviceInit(Integer totalTimeSeconds, Integer type){
        hvacOptimizerDeviceInitializationCacheDto.setElapsedTimeSeconds(0);
        hvacOptimizerDeviceInitializationCacheDto.setInProgress(true);
        hvacOptimizerDeviceInitializationCacheDto.setLog("");
        hvacOptimizerDeviceInitializationCacheDto.setRemainingTimeSeconds(totalTimeSeconds);
        hvacOptimizerDeviceInitializationCacheDto.setTotalTimeSeconds(totalTimeSeconds);
        hvacOptimizerDeviceInitializationCacheDto.setSyncedHvacDevices(new ArrayList<>());
        hvacOptimizerDeviceInitializationCacheDto.setType(type);
    }

    @Override
    public void onProgress(Integer elapsedTimeDelta, String log, HVACDevice syncedDevice){
        hvacOptimizerDeviceInitializationCacheDto.addTime(elapsedTimeDelta);
        if(log != null)
            hvacOptimizerDeviceInitializationCacheDto.addLog(log);
        if(syncedDevice != null)
            hvacOptimizerDeviceInitializationCacheDto.addDevice(pvOptimizerMapper.convertToDto(syncedDevice));
    }

    @Override
    public void endHvacDeviceInit(){
        pvoptimizerCacheService.initHvacDevicesCache();
        hvacOptimizerDeviceInitializationCacheDto.setInProgress(false);
        hvacOptimizerDeviceInitializationCacheDto.setRemainingTimeSeconds(0);
        hvacOptimizerDeviceInitializationCacheDto.setElapsedTimeSeconds(hvacOptimizerDeviceInitializationCacheDto.getTotalTimeSeconds());
    }
}
