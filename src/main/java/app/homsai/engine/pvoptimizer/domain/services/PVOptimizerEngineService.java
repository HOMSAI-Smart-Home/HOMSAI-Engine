package app.homsai.engine.pvoptimizer.domain.services;

import app.homsai.engine.common.domain.utils.Consts;
import org.springframework.scheduling.annotation.Scheduled;

public interface PVOptimizerEngineService {

    void updateHvacDeviceOptimizationCache();

    void updateHvacDeviceOptimizationStatus();
}
