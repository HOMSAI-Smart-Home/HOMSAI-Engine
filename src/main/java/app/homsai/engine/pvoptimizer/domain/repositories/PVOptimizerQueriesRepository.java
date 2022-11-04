package app.homsai.engine.pvoptimizer.domain.repositories;

import app.homsai.engine.pvoptimizer.domain.exceptions.HvacEntityNotFoundException;
import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import app.homsai.engine.pvoptimizer.domain.models.HVACEquipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */


public interface PVOptimizerQueriesRepository {

    Page<HVACDevice> findAllHvacDevices(Pageable pageable, String search);

    @Transactional
    HVACDevice findOneHvacDeviceByEntityIdAndType(String entityId, Integer type) throws HvacEntityNotFoundException;

    Page<HVACEquipment> findAllHvacEquipments(Pageable pageable, String search);

    HVACEquipment findOneHvacEquipment(String equipmentId);

}
