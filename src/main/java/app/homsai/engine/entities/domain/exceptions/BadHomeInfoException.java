package app.homsai.engine.entities.domain.exceptions;

import app.homsai.engine.common.domain.exceptions.NotFoundException;
import app.homsai.engine.common.domain.models.ErrorCodes;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */
public class BadHomeInfoException extends NotFoundException {
    private static final int ERROR_CODE = ErrorCodes.BAD_HOME_INFO;

    public BadHomeInfoException() {
        super(ERROR_CODE, "bad home info");
    }
}
