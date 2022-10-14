package app.homsai.engine.common.domain.utils.constants;

import org.springframework.stereotype.Service;

import static app.homsai.engine.common.domain.utils.constants.Consts.*;

@Service
public class ConstsUtilsImpl implements ConstsUtils {

    @Override
    public int getHvacPvOptimizationMinimumIdleMinutes() {
        return HVAC_PV_OPTIMIZATION_MINIMUM_IDLE_MINUTES;
    }

    @Override
    public int getHvacPvOptimizationMinimumExecutionMinutes() {
        return HVAC_PV_OPTIMIZATION_MINIMUM_EXECUTION_MINUTES;
    }

    @Override
    public Integer getHvacInitializationSleepTimeMillis() {
        return HVAC_INITIALIZATION_SLEEP_TIME_MILLIS;
    }

    @Override
    public Integer getHvacInitializationInfraTimeDurationMillis() {
        return HVAC_INITIALIZATION_INFRA_TIME_DURATION_MILLIS;
    }

    @Override
    public Integer getHvacInitializationDurationMinutes() {
        return HVAC_INITIALIZATION_DURATION_MINUTES;
    }

    @Override
    public Integer getHvacBcInitializationDurationMinutes() {
        return HVAC_BC_INITIALIZATION_DURATION_MINUTES;
    }

    @Override
    public int calcInitHvacDeviceCycles() {
        return getHvacInitializationDurationMinutes() * 60 / (getHvacInitializationSleepTimeMillis() / 1000);
    }

    @Override
    public int calcHvacDeviceCyclesForNextInit() {
        return calcInitHvacDeviceCycles() / 2;
    }

    @Override
    public int calcInitBaseConsumptionCycles() {
        return getHvacBcInitializationDurationMinutes() * 60 / (getHvacInitializationSleepTimeMillis() / 1000);
    }

    @Override
    public int getSyncHomeAssistantEntitiesLockTimeout() {
        return 30000;
    }

}
