package app.homsai.engine.pvoptimizer.domain.services;

import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface PVOptimizerCommandsService {


    @Async("threadPoolTaskExecutor")
    void initHomsaiHvacDevices(List<HVACDevice> hvacDeviceList, Integer type, String hvacFunction) throws InterruptedException, HvacPowerMeterIdNotSet, BadHomeInfoException;

    Double calculateInitTime(Integer deviceSize);

    void deleteFromHvacDevicesByType(Integer type);

    HVACDevice updateHvacDevice(HVACDevice hvacDevice);
}
