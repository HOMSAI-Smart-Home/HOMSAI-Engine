package app.homsai.engine.common.domain.exceptions;

public class BadRequestException extends ErrorInfoException {
    public BadRequestException(Integer code, String message) {
        super(code, message);
    }
}
