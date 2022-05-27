package app.homsai.engine.media.infrastructure.repositories;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

import app.homsai.engine.common.infrastructure.repositories.SoftDeletesRepository;
import app.homsai.engine.media.domain.models.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MediaQueriesJpaRepository extends SoftDeletesRepository<Media, String> {
    Page<Media> findByDeletedAtIsNull(Pageable pageRequest);
}
