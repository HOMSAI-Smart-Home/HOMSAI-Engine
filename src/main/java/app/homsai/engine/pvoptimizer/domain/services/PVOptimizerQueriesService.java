package app.homsai.engine.pvoptimizer.domain.services;

import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PVOptimizerQueriesService {


    Page<HVACDevice> findAllHomsaiHvacDevices(Pageable pageable, String search);

    HVACDevice findOneHvacDeviceByEntityId(String entityId);

}
