package app.homsai.engine.pvoptimizer.domain.exceptions;

import app.homsai.engine.common.domain.exceptions.NotFoundException;
import app.homsai.engine.common.domain.models.ErrorCodes;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */
public class ClimateEntityNotFoundException extends NotFoundException {
    private static final int ERROR_CODE = ErrorCodes.CLIMATE_ENTITY_NOT_FOUND;

    public ClimateEntityNotFoundException(Integer type) {
        super(ERROR_CODE, "climate entities for type" + type + " not found");
    }
}
