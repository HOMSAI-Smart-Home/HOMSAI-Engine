package app.homsai.engine.common.domain.exceptions.jwt;

import app.homsai.engine.common.domain.exceptions.UnauthorizedException;
import app.homsai.engine.common.domain.models.ErrorCodes;

public class BadCredentialsException extends UnauthorizedException {
    private static final int ERROR_CODE = ErrorCodes.BAD_CREDENTIALS_EXCEPTION;

    public BadCredentialsException() {
        super(ERROR_CODE, "Invalid username or password");
    }
}
