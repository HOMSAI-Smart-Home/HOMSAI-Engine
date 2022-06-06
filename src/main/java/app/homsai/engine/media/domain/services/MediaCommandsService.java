package app.homsai.engine.media.domain.services;

import app.homsai.engine.media.domain.exceptions.MediaNotSupportedException;
import app.homsai.engine.media.domain.exceptions.MediaNotFoundException;
import app.homsai.engine.media.domain.models.Media;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */
public interface MediaCommandsService {
    Media create(Media media, MultipartFile file) throws MediaNotSupportedException;

    void delete(Media media);

    void disassociateMediaEntity(String mediaUuid, String entityUuid) throws MediaNotFoundException;

    void associateMediaEntity(String mediaUuid, String entityUuid) throws MediaNotFoundException;

}
