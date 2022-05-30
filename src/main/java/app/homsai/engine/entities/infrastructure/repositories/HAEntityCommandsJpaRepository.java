package app.homsai.engine.entities.infrastructure.repositories;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

import app.homsai.engine.common.infrastructure.repositories.SoftDeletesRepository;
import app.homsai.engine.entities.domain.models.HAEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface HAEntityCommandsJpaRepository
        extends SoftDeletesRepository<HAEntity, String> {

    @Modifying
    @Query(
            value = "TRUNCATE table entities_areas;" +
                    "DELETE FROM entities;",
            nativeQuery = true
    )
    void truncate();
}
