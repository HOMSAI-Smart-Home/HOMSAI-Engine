package app.homsai.engine.common.domain.exceptions;

import app.homsai.engine.common.domain.models.ErrorCodes;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */
public class TokenIsNullException extends NotFoundException {
    private static final int ERROR_CODE = ErrorCodes.TOKEN_IS_NULL;

    public TokenIsNullException() {
        super(ERROR_CODE, "Token is null");
    }
}
