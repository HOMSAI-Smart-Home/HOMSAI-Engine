package app.homsai.engine.entities.domain.exceptions;

import app.homsai.engine.common.domain.exceptions.NotFoundException;
import app.homsai.engine.common.domain.models.ErrorCodes;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */
public class AreaNotFoundException extends NotFoundException {
    private static final int ERROR_CODE = ErrorCodes.AREA_NOT_FOUND;

    public AreaNotFoundException(String mediaUuid) {
        super(ERROR_CODE, "area " + mediaUuid + " not found");
    }
}
