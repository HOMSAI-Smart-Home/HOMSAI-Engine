package app.homsai.engine.pvoptimizer.domain.services;

import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface PVOptimizerCommandsService {


    @Async("threadPoolTaskExecutor")
    void initHomsaiHvacDevices(List<HVACDevice> hvacDeviceList, String hvacFunction) throws InterruptedException, HvacPowerMeterIdNotSet;

    Double calculateInitTime(Integer deviceSize);

    void deleteFromHvacDevicesByType(Integer type);

    HVACDevice updateHvacDevice(HVACDevice hvacDevice);
}
