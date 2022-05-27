package app.homsai.engine.common.domain.models;

/**
 * Created by Giacomo Agostini on 29/05/2017.
 */
public final class SystemConsts {

    /* ASSET CATEGORY */
    public final static String SYSTEM_LOCATION = "0";
    public final static String SYSTEM_MAIN_OFFICE = "1";
    public final static String SYSTEM_OFFICE = "2";
    public final static String SYSTEM_GENERAL = "3";
    public final static String SYSTEM_CONTAINER = "4";
    public final static String SYSTEM_VEHICLE = "5";

    /* TASK STATUS */
    public final static int SYSTEM_TASK_REJECTED = 0;
    public final static int SYSTEM_TASK_ABORTED = 1;

    public final static int SYSTEM_TASK_PENDING = 100;
    public final static int SYSTEM_TASK_OPEN = 110;

    public final static int SYSTEM_TASK_ASSIGNED = 200;
    public final static int SYSTEM_TASK_IN_CHARGED = 210;

    public final static int SYSTEM_TASK_PAUSED = 300;
    public final static int SYSTEM_TASK_IN_PROGRESS = 310;

    public final static int SYSTEM_TASK_CLOSED = 400;
    public final static int SYSTEM_TASK_ARCHIVED = 410;

    public final static int SYSTEM_TASK_OVERDUE = 900;

    /* JOB STATUS */
    public final static Integer SYSTEM_JOB_OPEN = 100;
    public final static Integer SYSTEM_JOB_CLOSED = 200;

    /* SLA */
    public final static String SYSTEM_SLA_ANALYSIS = "0";
    public final static String SYSTEM_SLA_IN_CHARGE = "1";
    public final static String SYSTEM_SLA_RESOLUTION = "2";

    /* SCHEDULER TYPE */
    public final static Integer SYSTEM_TASK_SCHEDULER_TIME_CONDITION_BASED = 0;
    public final static Integer SYSTEM_TASK_SCHEDULER_ASSET_COUNTER_BASED = 1;
    public final static Integer SYSTEM_TASK_SCHEDULER_ASSET_TRIGGER_BASED = 2;

    /* PERMISSION */
    public final static Integer SYSTEM_PERMISSION_NUMBER = 3;

    /* STOCK */
    public final static Integer SYSTEM_STOCK_BY_LOCATIONS = 0;
    public final static Integer SYSTEM_STOCK_BY_RESOURCES = 1;

    /* ROLE PERMISSION */
    public final static String SYSTEM_ROLE_ADMIN = "ADMIN";
    public final static String SYSTEM_ROLE_USER = "USER";

    /* JOB AND TASK CAUSAL CODE */
    public final static Integer CAUSAL_CODE_LIMIT_INF = -1;
    public final static Integer CAUSAL_CODE_FREE = -1;
    public final static Integer CAUSAL_CODE_MAINTENANCE = 0;
    public final static Integer CAUSAL_CODE_ALLOCATION = 1;
    public final static Integer CAUSAL_CODE_DEALLOCATION = 2;
    public final static Integer CAUSAL_CODE_INVENTORY = 3;
    public final static Integer CAUSAL_CODE_PRODUCTION = 4;

    public final static Integer CAUSAL_CODE_LIMIT_SUP = 4;


    public final static String andDivider = ",";
    public final static String orDivider = ";";
    public final static String searchRegEx =
            "(\\w+?)(:|<|>|!:)(\\S+?)(" + andDivider + "|" + orDivider + ")";

    public final static Character RESOURCE_ROLE_ASSET = 'A';
    public final static Character RESOURCE_ROLE_TARGET = 'T';
    public final static Character RESOURCE_ROLE_PART = 'P';
    public final static Character RESOURCE_ROLE_WATCHER = 'W';
    public final static Character RESOURCE_ROLE_GENERATED = 'G';
    public final static Character RESOURCE_TYPE_USER = 'U';
    public final static Character RESOURCE_TYPE_TEAM = 'T';
    public final static Character RESOURCE_TYPE_OBJECT = 'O';
    public final static Character RESOURCE_ROLE_LOCATION = 'L';
    public final static Character RESOURCE_ROLE_DUMMY = 'Z';
}
