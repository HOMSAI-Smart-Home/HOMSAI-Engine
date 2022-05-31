package app.homsai.engine.entities.infrastructure.repositories;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

import app.homsai.engine.common.infrastructure.repositories.SoftDeletesRepository;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.models.HomsaiEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface HomsaiEntityCommandsJpaRepository
        extends SoftDeletesRepository<HomsaiEntity, String> {

    @Modifying
    @Query(
            value = "TRUNCATE table ha_entities_homsai_entities;" +
                    "DELETE FROM homsai_entities;",
            nativeQuery = true
    )
    void truncate();
}
