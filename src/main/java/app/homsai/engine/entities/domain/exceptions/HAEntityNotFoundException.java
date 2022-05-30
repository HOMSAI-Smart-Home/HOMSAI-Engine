package app.homsai.engine.entities.domain.exceptions;

import app.homsai.engine.common.domain.exceptions.NotFoundException;
import app.homsai.engine.common.domain.models.ErrorCodes;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */
public class HAEntityNotFoundException extends NotFoundException {
    private static final int ERROR_CODE = ErrorCodes.HAENTITY_NOT_FOUND;

    public HAEntityNotFoundException(String mediaUuid) {
        super(ERROR_CODE, "entity " + mediaUuid + " not found");
    }
}
