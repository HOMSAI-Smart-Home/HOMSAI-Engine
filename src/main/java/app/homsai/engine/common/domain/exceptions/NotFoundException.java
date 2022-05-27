package app.homsai.engine.common.domain.exceptions;

public class NotFoundException extends ErrorInfoException {
    public NotFoundException(Integer code, String message) {
        super(code, message);
    }
}
