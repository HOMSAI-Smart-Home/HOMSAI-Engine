package app.homsai.engine.common.domain.utils;

public class Consts {

    public static final String HOME_INFO_UUID = "4dc3ff72-d861-4d29-855f-fee6d32530b4";
    public static final String HOME_AREA_UUID = "70b14a1b-811c-44e5-9d92-7226683a7ceb";

    public static final int HVAC_DEVICE_HEATING = 0;
    public static final int HVAC_DEVICE_CONDITIONING = 1;

    public static final String HOME_ASSISTANT_HVAC_DEVICE_HEATING_FUNCTION = "heat";
    public static final String HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION = "cool";
    public static final String HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION = "off";

    public static final String HOME_ASSISTANT_WATT = "W";



    public static final Integer HVAC_INITIALIZATION_SLEEP_TIME_MILLIS = 30*1000;
    public static final Integer HVAC_INITIALIZATION_INFRA_TIME_DURATION_MILLIS = 5*60*1000;
    public static final Integer HVAC_INITIALIZATION_DURATION_MINUTES = 10;
    public static final Integer HVAC_BC_INITIALIZATION_DURATION_MINUTES = 5;
    public static final Integer HVAC_INITIALIZATION_WATT_THRESHOLD = 200;
}
