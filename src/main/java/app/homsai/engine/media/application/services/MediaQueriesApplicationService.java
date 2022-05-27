package app.homsai.engine.media.application.services;

import app.homsai.engine.media.application.http.dtos.MediaQueriesDto;
import app.homsai.engine.media.domain.exceptions.MediaNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Created by Giacomo Agostini on 02/12/16.
 */


public interface MediaQueriesApplicationService {
    Page<MediaQueriesDto> findAll(Pageable pageRequest, String search, String entityUuid)
            throws Exception;

    MediaQueriesDto findOne(String mediaUuid) throws Exception;

    Resource getFile(String mediaUuid, String res) throws MediaNotFoundException;
}
