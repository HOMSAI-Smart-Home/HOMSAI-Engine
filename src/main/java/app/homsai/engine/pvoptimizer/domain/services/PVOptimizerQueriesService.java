package app.homsai.engine.pvoptimizer.domain.services;

import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import app.homsai.engine.pvoptimizer.domain.models.HVACEquipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


public interface PVOptimizerQueriesService {


    Page<HVACDevice> findAllHomsaiHvacDevices(Pageable pageable, String search);

    @Transactional
    HVACDevice findOneHvacDeviceByEntityIdAndType(String entityId, Integer type);

    Page<HVACEquipment> findAllHomsaiHvacEquipments(Pageable pageable, String search);

    HVACEquipment findOneHvacEquipment(String equipmentid);

}
