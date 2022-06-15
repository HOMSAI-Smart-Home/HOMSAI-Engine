package app.homsai.engine.optimizations.infrastructure.cache;


import app.homsai.engine.optimizations.domain.models.HvacDevice;

import java.util.HashMap;
import java.util.List;

public interface HVACRunningDevicesCacheRepository {

    HashMap<String ,HvacDevice> getHvacDevicesCache();

    void initHvacDevicesCache();

    void updateHvacDevicesCache();
}
