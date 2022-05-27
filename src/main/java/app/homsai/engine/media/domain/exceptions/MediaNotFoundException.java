package app.homsai.engine.media.domain.exceptions;

import app.homsai.engine.common.domain.exceptions.NotFoundException;
import app.homsai.engine.common.domain.models.ErrorCodes;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */
public class MediaNotFoundException extends NotFoundException {
    private static final int ERROR_CODE = ErrorCodes.MEDIA_NOT_FOUND;

    public MediaNotFoundException(String mediaUuid) {
        super(ERROR_CODE, "Media " + mediaUuid + " not found");
    }
}
