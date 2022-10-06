package app.homsai.engine.forecast.domain.services.cache;

import app.homsai.engine.forecast.domain.models.PVOptimizationForecastCache;
import app.homsai.engine.forecast.domain.models.ProductionConsumptionCache;

/**
 * @author Giacomo Agostini on 03/10/2022
 */
public interface ForecastCacheService {
    PVOptimizationForecastCache getPvOptimizationForecastCache();

    ProductionConsumptionCache getProductionConsumptionCache();

    void syncHomsaiOptimizationForecast();

    void syncHomsaiProductionConsumptionForecast();
}
