package app.homsai.engine.media.domain.repositories;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

import app.homsai.engine.media.domain.exceptions.MediaNotFoundException;
import app.homsai.engine.media.domain.models.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MediaQueriesRepository {
    Page<Media> findAll(Pageable pageRequest, String search, String entityUuid);

    Media findOne(String mediaUuid) throws MediaNotFoundException;
}
