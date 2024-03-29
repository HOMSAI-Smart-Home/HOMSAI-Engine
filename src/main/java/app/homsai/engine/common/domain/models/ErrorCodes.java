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
     * Entities
     */
    public static final int AREA_NOT_FOUND = 31;
    public static final int HAENTITY_NOT_FOUND = 32;
    public static final int BAD_HOME_INFO = 33;
    public static final int HVAC_POWER_METER_NOT_SET = 34;
    public static final int BAD_INTERVALS = 35;
    public static final int HVAC_ENTITY_NOT_FOUND = 36;
    public static final int CLIMATE_ENTITY_NOT_FOUND = 37;


    /*
     * JWT / Validation
     */
    public static final int AUTHENTICATION_EXCEPTION = 100;
    public static final int AUTH_METHOD_NOT_SUPPORTED_EXCEPTION = 101;
    public static final int BAD_CREDENTIALS_EXCEPTION = 102;
    public static final int JWT_EXPIRED_TOKEN_EXCEPTION = 103;
    public static final int ACCESS_DENIED_EXCEPTION = 104;
    public static final int VALIDATION_FAILED_EXCEPTION = 105;
    public static final int TOKEN_IS_NULL = 106;


}
