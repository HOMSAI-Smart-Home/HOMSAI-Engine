package app.homsai.engine.common.domain.utils;

import com.vaadin.flow.component.Component;

public class EnText {
    public static final String CLOSE = "Close";
    public static final String CANCEL = "Cancel";
    public static final String OK = "Ok";
    public static final String CONTINUE = "Continue";
    public static final String START_HVAC_DEVICE_INIT_TITLE = "HVAC devices initialization";
    public static final String START_HVAC_DEVICE_INIT_DESCRIPTION = "HVAC devices initialization measures the real power consumption of each HVAC device.<br>" +
            "To do this, Homsai turns on each device for "+Consts.HVAC_INITIALIZATION_DURATION_MINUTES+" minutes, so the total time required by this task is ARG0 minutes.<br>" +
            "All work will be done in background. Are you sure to continue?";

    public static final String ERROR_TITLE = "Error";
    public static final String ERROR_HVAC_DEVICE_INIT_DESCRIPTION = "You have to configure:<br>" +
            "<b>HVAC power meter sensor</b><br>" +
            "to initialize HVAC devices";
    public static final String ERROR_PV_OPT_START_DESCRIPTION = "You have to configure:<br>" +
            "<b>General power meter sensor</b><br>" +
            "<b>HVAC power meter sensor</b><br>" +
            "<b>Photovoltaic production power meter sensor</b><br>" +
            "to enable Homsai Photovoltaic Optimizer";
    public static final String SELECT_GENERAL_PM_LABEL = "General Power Meter";
    public static final String SELECT_HVAC_PM_LABEL = "HVAC Power Meter";
    public static final String SELECT_PV_PRODUCTION_PM_LABEL = "Photovoltaic Production Power Meter";
    public static final String SELECT_STORAGE_PM_LABEL = "Photovoltaic Storage Power Meter";
    public static final String NO_SENSOR = "No sensor";
    public static final String DATA_SAVED = "Data saved correctly";
    public static final String ERROR_DATA_SAVE = "Error: please check input values";
    public static final String SAVE = "Save";
    public static final String LOGOUT_TITLE = "Logout";
    public static final String LOGOUT_TEXT = "Are you sure you want to log out?";
    public static final String WAITING_FIRST_SYNC = "First sync in progress.. Please wait";
}
