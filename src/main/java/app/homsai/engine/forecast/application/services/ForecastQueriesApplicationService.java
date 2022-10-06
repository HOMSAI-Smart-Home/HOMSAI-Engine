package app.homsai.engine.forecast.application.services;

import app.homsai.engine.forecast.domain.models.PVOptimizationForecastCache;
import app.homsai.engine.forecast.domain.models.ProductionConsumptionCache;

/**
 * @author Giacomo Agostini on 03/10/2022
 */
public interface ForecastQueriesApplicationService {
    PVOptimizationForecastCache getOptimizerForecast();

    ProductionConsumptionCache getProductionConsumptionForecast();
}
