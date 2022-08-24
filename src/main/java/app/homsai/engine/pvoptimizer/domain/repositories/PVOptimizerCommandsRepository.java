package app.homsai.engine.pvoptimizer.domain.repositories;

import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */


public interface PVOptimizerCommandsRepository {

    HVACDevice saveHvacDevice(HVACDevice hvacDevice);

    void deleteFromHvacDevicesByType(Integer type);

    HVACDevice updateHvacDevice(HVACDevice hvacDevice);

}
