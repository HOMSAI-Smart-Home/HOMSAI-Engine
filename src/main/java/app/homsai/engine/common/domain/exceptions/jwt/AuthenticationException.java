package app.homsai.engine.common.domain.exceptions.jwt;

import app.homsai.engine.common.domain.exceptions.UnauthorizedException;
import app.homsai.engine.common.domain.models.ErrorCodes;

public class AuthenticationException extends UnauthorizedException {
    private static final int ERROR_CODE = ErrorCodes.AUTHENTICATION_EXCEPTION;

    public AuthenticationException() {
        super(ERROR_CODE, "Authentication failed");
    }
}
