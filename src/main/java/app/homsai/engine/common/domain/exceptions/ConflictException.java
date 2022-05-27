package app.homsai.engine.common.domain.exceptions;

public class ConflictException extends ErrorInfoException {
    public ConflictException(Integer code, String message) {
        super(code, message);
    }
}
