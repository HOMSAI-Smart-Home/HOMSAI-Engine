package app.homsai.engine.pvoptimizer.infrastructure.repositories;


import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import app.homsai.engine.pvoptimizer.domain.repositories.PVOptimizerCommandsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Repository
public class PVOptimizerCommandsRepositoryImpl implements PVOptimizerCommandsRepository {

    @Autowired
    HVACDeviceCommandsJpaRepository hvacDeviceCommandsJpaRepository;


    @Override
    public HVACDevice saveHvacDevice(HVACDevice hvacDevice) {
        return hvacDeviceCommandsJpaRepository.saveAndFlushNow(hvacDevice);
    }

    @Override
    public void deleteFromHvacDevicesByType(Integer type) {
        List<HVACDevice> hvacDeviceList = hvacDeviceCommandsJpaRepository.findAllActiveList(Pageable.unpaged(), "type:"+type, null);
        hvacDeviceCommandsJpaRepository.deleteAll(hvacDeviceList);
    }

    @Override
    public HVACDevice updateHvacDevice(HVACDevice hvacDevice) {
        return hvacDeviceCommandsJpaRepository.save(hvacDevice);
    }


}
