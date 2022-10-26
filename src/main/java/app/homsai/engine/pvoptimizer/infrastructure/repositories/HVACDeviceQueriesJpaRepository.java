package app.homsai.engine.pvoptimizer.infrastructure.repositories;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

import app.homsai.engine.common.infrastructure.repositories.SoftDeletesRepository;
import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import org.springframework.transaction.annotation.Transactional;

public interface HVACDeviceQueriesJpaRepository
        extends SoftDeletesRepository<HVACDevice, String> {

    @Transactional
    HVACDevice findOneByEntityIdAndType(String entityId, Integer type);
}
