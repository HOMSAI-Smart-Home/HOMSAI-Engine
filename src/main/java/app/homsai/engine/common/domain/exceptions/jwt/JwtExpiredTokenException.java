package app.homsai.engine.common.domain.exceptions.jwt;

import app.homsai.engine.common.domain.exceptions.UnauthorizedException;
import app.homsai.engine.common.domain.models.ErrorCodes;

public class JwtExpiredTokenException extends UnauthorizedException {
    private static final int ERROR_CODE = ErrorCodes.JWT_EXPIRED_TOKEN_EXCEPTION;

    public JwtExpiredTokenException() {
        super(ERROR_CODE, "Token has expired");
    }
}

