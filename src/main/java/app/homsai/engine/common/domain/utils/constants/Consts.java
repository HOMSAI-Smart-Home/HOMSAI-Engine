package app.homsai.engine.common.domain.utils.constants;

public class Consts {

    public static final String HOME_INFO_UUID = "4dc3ff72-d861-4d29-855f-fee6d32530b4";
    public static final String HOME_AREA_UUID = "70b14a1b-811c-44e5-9d92-7226683a7ceb";

    public static final String HOME_ASSISTANT_HVAC_DEVICE_HEATING_FUNCTION = "heat";
    public static final String HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION = "cool";
    public static final String HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION = "off";
    public static final String HOME_ASSISTANT_HVAC_DEVICE_IDLE_FUNCTION = "idle";

    public static final String HOME_ASSISTANT_WATT = "W";

    public static final int HVAC_PV_OPTIMIZATION_UPDATE_TIMEDELTA_MINUTES = 5;
    public static final int HVAC_PV_OPTIMIZATION_REQUEST_TIMEDELTA_MINUTES = 15;
    public static final int HVAC_PV_OPTIMIZATION_MINIMUM_IDLE_MINUTES = 25;
    public static final int HVAC_PV_OPTIMIZATION_MINIMUM_EXECUTION_MINUTES = 25;

    public static final Double HVAC_INITIALIZATION_MIN_CONSUMPTION = 250D;
    public static final Integer HVAC_INITIALIZATION_SLEEP_TIME_MILLIS = 30*1000;

    public static final Integer HVAC_INITIALIZATION_DURATION_MINUTES = 30; //30
    public static final Integer HVAC_BC_INITIALIZATION_DURATION_MINUTES = 5; //5
    public static final Integer HVAC_INITIALIZATION_WATT_THRESHOLD = 200;
    public static final Double HVAC_INITIALIZATION_WATT_DEFAULT = 500D;
    public static final Double HVAC_INITIALIZATION_WATT_DEFAULT_COUPLED = 250D;
    public static final int HVAC_MODE_WINTER_ID = 0;
    public static final int HVAC_MODE_SUMMER_ID = 1;

    public static final int PV_OPTIMIZATION_MODE_WINTER = 0;
    public static final int PV_OPTIMIZATION_MODE_SUMMER = 1;

    public static final Double HVAC_THRESHOLD_TEMPERATURE = 0D;

    public static final String HOME_ASSISTANT_EUR_UNIT = "EUR";
    public static final String EUR_UNIT = "EU";
    public static final String US_UNIT = "US";

}
