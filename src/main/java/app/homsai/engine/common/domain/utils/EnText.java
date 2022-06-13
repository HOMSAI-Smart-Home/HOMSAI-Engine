package app.homsai.engine.common.domain.utils;

public class EnText {
    public static final String CLOSE = "close";
    public static final String CANCEL = "cancel";
    public static final String OK = "ok";
    public static final String CONTINUE = "continue";
    public static final String START_HVAC_DEVICE_INIT_TITLE = "HVAC devices initialization";
    public static final String START_HVAC_DEVICE_INIT_DESCRIPTION = "HVAC devices initialization measures the real power consumption of each HVAC device.<br>" +
            "In order to do this Homsai turns on each device for "+Consts.HVAC_INITIALIZATION_DURATION_MINUTES+" minutes, so the total time required by this task is ARG0 minutes<br>" +
            "Are you sure to continue?";
}
