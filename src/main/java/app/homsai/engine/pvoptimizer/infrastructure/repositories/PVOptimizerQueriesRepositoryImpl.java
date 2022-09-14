package app.homsai.engine.pvoptimizer.infrastructure.repositories;

import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import app.homsai.engine.pvoptimizer.domain.models.HVACEquipment;
import app.homsai.engine.pvoptimizer.domain.repositories.PVOptimizerQueriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Repository
public class PVOptimizerQueriesRepositoryImpl implements PVOptimizerQueriesRepository {

    @Autowired
    HVACDeviceQueriesJpaRepository hvacDeviceQueriesJpaRepository;

    @Autowired
    HVACEquipmentQueriesJpaRepository hvacEquipmentQueriesJpaRepository;

    @Override
    @Transactional
    public Page<HVACDevice> findAllHvacDevices(Pageable pageable, String search) {
        return hvacDeviceQueriesJpaRepository.findAllActive(pageable, search);
    }

    @Override
    @Transactional
    public HVACDevice findOneHvacDeviceByEntityId(String entityId) {
        return hvacDeviceQueriesJpaRepository.findOneByEntityId(entityId);
    }

    @Override
    public Page<HVACEquipment> findAllHvacEquipments(Pageable pageable, String search) {
        return hvacEquipmentQueriesJpaRepository.findAllActive(pageable, search);
    }

    @Override
    public HVACEquipment findOneHvacEquipment(String equipmentId) {
        return hvacEquipmentQueriesJpaRepository.findOneActive(equipmentId);
    }

}
