package app.homsai.engine.entities.infrastructure.repositories;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

import app.homsai.engine.common.infrastructure.repositories.SoftDeletesRepository;
import app.homsai.engine.entities.domain.models.Area;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AreaCommandsJpaRepository
        extends SoftDeletesRepository<Area, String> {

    @Modifying
    @Query(
            value = "TRUNCATE TABLE AREAS",
            nativeQuery = true
    )
    void truncate();
}
