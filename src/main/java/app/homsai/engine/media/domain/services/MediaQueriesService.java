package app.homsai.engine.media.domain.services;

import app.homsai.engine.media.domain.exceptions.MediaNotFoundException;
import app.homsai.engine.media.domain.models.Media;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */
public interface MediaQueriesService {
    Page<Media> findAll(Pageable pageRequest, String search, String entityUuid);

    Media findOne(String mediaUuid) throws MediaNotFoundException;

    Resource getFile(String mediaUuid, String res) throws MediaNotFoundException;

    String getFilePath(String filename);
}
