package app.homsai.engine.common.domain.exceptions;

public class UnauthorizedException extends ErrorInfoException {
    public UnauthorizedException(Integer code, String message) {
        super(code, message);
    }
}
