package app.homsai.engine.pvoptimizer.infrastructure.repositories;

import app.homsai.engine.common.infrastructure.repositories.SoftDeletesRepository;
import app.homsai.engine.pvoptimizer.domain.models.HVACEquipment;

public interface HVACEquipmentQueriesJpaRepository
        extends SoftDeletesRepository<HVACEquipment, String> {

}
