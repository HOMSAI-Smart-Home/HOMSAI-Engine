package app.homsai.engine.common.domain.exceptions;

import java.io.IOException;

abstract class ErrorInfoException extends IOException {
    private Integer code;

    public ErrorInfoException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
