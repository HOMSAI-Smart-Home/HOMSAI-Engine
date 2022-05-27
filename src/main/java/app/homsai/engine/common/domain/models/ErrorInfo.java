package app.homsai.engine.common.domain.models;

public class ErrorInfo {
    public final Integer code;
    public final String message;

    public ErrorInfo(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
