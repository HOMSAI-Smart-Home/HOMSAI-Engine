package app.homsai.engine.common.domain.exceptions;

import app.homsai.engine.common.domain.models.ErrorCodes;

public class AccessDeniedException extends ErrorInfoException {
    private static final int ERROR_CODE = ErrorCodes.ACCESS_DENIED_EXCEPTION;

    public AccessDeniedException() {
        super(ERROR_CODE, "Access Denied");
    }
}
