package app.homsai.engine.pvoptimizer.domain.exceptions;

import app.homsai.engine.common.domain.exceptions.NotFoundException;
import app.homsai.engine.common.domain.models.ErrorCodes;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */
public class HvacEntityNotFoundException extends NotFoundException {
    private static final int ERROR_CODE = ErrorCodes.HVAC_ENTITY_NOT_FOUND;

    public HvacEntityNotFoundException(String mediaUuid) {
        super(ERROR_CODE, "hvac entity " + mediaUuid + " not found");
    }
}
