package app.homsai.engine.pvoptimizer.domain.services.cache;


import app.homsai.engine.pvoptimizer.domain.models.OptimizerHVACDevice;

import java.util.HashMap;

public interface PVOptimizerCacheService {

    HashMap<String , OptimizerHVACDevice> getHvacDevicesCache();

    void initHvacDevicesCache();

    void updateHvacDevicesCache();
}
