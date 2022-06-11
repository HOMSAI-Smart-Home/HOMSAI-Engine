package app.homsai.engine.entities.application.http.cache;

import app.homsai.engine.entities.application.http.converters.EntitiesMapper;
import app.homsai.engine.entities.application.http.dtos.HvacDeviceCacheDto;
import app.homsai.engine.entities.domain.models.HVACDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class HomsaiHVACDeviceCacheRepositoryImpl implements HomsaiHVACDeviceCacheRepository{

    @Autowired
    EntitiesMapper entitiesMapper;

    private static final HvacDeviceCacheDto hvacDeviceCacheDto = new HvacDeviceCacheDto();

    @Override
    public HvacDeviceCacheDto getHvacDeviceCacheDto() {
        return hvacDeviceCacheDto;
    }

    @Override
    public void startHvacDeviceInit(Integer totalTimeSeconds){
        hvacDeviceCacheDto.setInProgress(true);
        hvacDeviceCacheDto.setLog("");
        hvacDeviceCacheDto.setRemainingTimeSeconds(totalTimeSeconds);
        hvacDeviceCacheDto.setTotalTimeSeconds(totalTimeSeconds);
        hvacDeviceCacheDto.setSyncedHvacDevices(new ArrayList<>());
    }

    @Override
    public void onProgress(Integer elapsedTimeDelta, String log, HVACDevice syncedDevice){
        hvacDeviceCacheDto.addTime(elapsedTimeDelta);
        if(log != null)
            hvacDeviceCacheDto.addLog(log);
        if(syncedDevice != null)
            hvacDeviceCacheDto.addDevice(entitiesMapper.convertToDto(syncedDevice));
    }

    @Override
    public void endHvacDeviceInit(){
        hvacDeviceCacheDto.setInProgress(false);
        hvacDeviceCacheDto.setRemainingTimeSeconds(0);
        hvacDeviceCacheDto.setElapsedTimeSeconds(hvacDeviceCacheDto.getTotalTimeSeconds());
    }
}
