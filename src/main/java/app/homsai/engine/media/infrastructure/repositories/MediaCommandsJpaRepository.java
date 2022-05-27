package app.homsai.engine.media.infrastructure.repositories;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

import app.homsai.engine.common.infrastructure.repositories.SoftDeletesRepository;
import app.homsai.engine.media.domain.models.Media;

public interface MediaCommandsJpaRepository extends SoftDeletesRepository<Media, Long> {
}
