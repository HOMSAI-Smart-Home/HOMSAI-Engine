package app.homsai.engine.common.domain.models;

public class ErrorCodes {
    /*
     * Media
     */
    public static final int MEDIA_NOT_FOUND = 1;
    public static final int MEDIA_NOT_SUPPORTED = 2;

    /*
     * Users
     */
    public static final int USER_ALREADY_REGISTERED = 20;
    public static final int USER_NOT_FOUND = 21;

    /*
     * JWT / Validation
     */
    public static final int AUTHENTICATION_EXCEPTION = 100;
    public static final int AUTH_METHOD_NOT_SUPPORTED_EXCEPTION = 101;
    public static final int BAD_CREDENTIALS_EXCEPTION = 102;
    public static final int JWT_EXPIRED_TOKEN_EXCEPTION = 103;
    public static final int ACCESS_DENIED_EXCEPTION = 104;
    public static final int VALIDATION_FAILED_EXCEPTION = 105;
}
