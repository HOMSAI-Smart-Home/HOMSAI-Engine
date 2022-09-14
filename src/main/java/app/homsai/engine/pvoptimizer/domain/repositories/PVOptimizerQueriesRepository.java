package app.homsai.engine.pvoptimizer.domain.repositories;

import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import app.homsai.engine.pvoptimizer.domain.models.HVACEquipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */


public interface PVOptimizerQueriesRepository {

    Page<HVACDevice> findAllHvacDevices(Pageable pageable, String search);

    HVACDevice findOneHvacDeviceByEntityId(String entityId);

    Page<HVACEquipment> findAllHvacEquipments(Pageable pageable, String search);

    HVACEquipment findOneHvacEquipment(String equipmentId);

}