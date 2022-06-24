package app.homsai.engine.entities.infrastructure.repositories;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

import app.homsai.engine.common.infrastructure.repositories.SoftDeletesRepository;
import app.homsai.engine.entities.domain.models.Area;
import org.springframework.transaction.annotation.Transactional;

public interface AreaQueriesJpaRepository
        extends SoftDeletesRepository<Area, String> {

    @Transactional
    Area findOneByNameAndDeletedAtIsNull(String name);
}
