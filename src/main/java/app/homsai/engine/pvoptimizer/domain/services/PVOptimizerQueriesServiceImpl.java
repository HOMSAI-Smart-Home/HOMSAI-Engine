package app.homsai.engine.pvoptimizer.domain.services;

import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import app.homsai.engine.pvoptimizer.domain.models.HVACEquipment;
import app.homsai.engine.pvoptimizer.domain.repositories.PVOptimizerQueriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PVOptimizerQueriesServiceImpl implements PVOptimizerQueriesService {

    @Autowired
    PVOptimizerQueriesRepository pvOptimizerQueriesRepository;



    @Override
    @Transactional
    public Page<HVACDevice> findAllHomsaiHvacDevices(Pageable pageable, String search) {
        return pvOptimizerQueriesRepository.findAllHvacDevices(pageable, search);
    }


    @Override
    @Transactional
    public HVACDevice findOneHvacDeviceByEntityId(String entityId) {
        return pvOptimizerQueriesRepository.findOneHvacDeviceByEntityId(entityId);
    }

    @Override
    @Transactional
    public Page<HVACEquipment> findAllHomsaiHvacEquipments(Pageable pageable, String search) {
        return pvOptimizerQueriesRepository.findAllHvacEquipments(pageable, search);
    }

    @Override
    @Transactional
    public HVACEquipment findOneHvacEquipment(String equipmentId) {
        return pvOptimizerQueriesRepository.findOneHvacEquipment(equipmentId);
    }

}
