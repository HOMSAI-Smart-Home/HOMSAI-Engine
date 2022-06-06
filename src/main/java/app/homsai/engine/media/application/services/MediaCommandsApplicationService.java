package app.homsai.engine.media.application.services;

import app.homsai.engine.media.application.http.dtos.MediaQueriesDto;
import app.homsai.engine.media.domain.exceptions.MediaNotFoundException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */
public interface MediaCommandsApplicationService {
    MediaQueriesDto create(MultipartFile mediaCreateCommandDto, String uuid, String tag)
            throws Exception;

    void delete(String mediaUuid) throws MediaNotFoundException;

    void associateMediaEntity(String mediaUuid, String entityUuid) throws MediaNotFoundException;

    void disassociateMediaEntity(String mediaUuid, String entityUuid) throws MediaNotFoundException;

}
