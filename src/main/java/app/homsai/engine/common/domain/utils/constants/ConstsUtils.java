package app.homsai.engine.common.domain.utils.constants;

public interface ConstsUtils {

    int getHvacPvOptimizationMinimumIdleMinutes();

    int getHvacPvOptimizationMinimumExecutionMinutes();

    Integer getHvacInitializationSleepTimeMillis();

    Integer getHvacInitializationInfraTimeDurationMillis();

    Integer getHvacInitializationDurationMinutes();

    Integer getHvacBcInitializationDurationMinutes();

    int calcInitHvacDeviceCycles();

    int calcHvacDeviceCyclesForNextInit();

    int calcInitBaseConsumptionCycles();

    int getSyncHomeAssistantEntitiesLockTimeout();

}
