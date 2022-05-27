package app.homsai.engine.media.domain.exceptions;

import app.homsai.engine.common.domain.exceptions.BadRequestException;
import app.homsai.engine.common.domain.models.ErrorCodes;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */
public class MediaNotSupportedException extends BadRequestException {
    private static final int ERROR_CODE = ErrorCodes.MEDIA_NOT_SUPPORTED;

    public MediaNotSupportedException(String mediaUuid) {
        super(ERROR_CODE, "Mimetype " + mediaUuid + " not supported");
    }
}
