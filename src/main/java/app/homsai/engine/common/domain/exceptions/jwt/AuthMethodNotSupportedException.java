package app.homsai.engine.common.domain.exceptions.jwt;

import app.homsai.engine.common.domain.exceptions.UnauthorizedException;
import app.homsai.engine.common.domain.models.ErrorCodes;

public class AuthMethodNotSupportedException extends UnauthorizedException {
    private static final int ERROR_CODE = ErrorCodes.AUTH_METHOD_NOT_SUPPORTED_EXCEPTION;

    public AuthMethodNotSupportedException() {
        super(ERROR_CODE, "Authentication method not supported");
    }
}
