package app.homsai.engine.forecast.application.services;

import app.homsai.engine.forecast.domain.models.PVOptimizationForecastCache;
import app.homsai.engine.forecast.domain.models.ProductionConsumptionCache;
import app.homsai.engine.forecast.domain.services.cache.ForecastCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Giacomo Agostini on 03/10/2022
 */

@Service
public class ForecastQueriesApplicationServiceImpl implements ForecastQueriesApplicationService{

    @Autowired
    ForecastCacheService forecastCacheService;

    @Override
    public PVOptimizationForecastCache getOptimizerForecast() {
        return forecastCacheService.getPvOptimizationForecastCache();
    }

    @Override
    public ProductionConsumptionCache getProductionConsumptionForecast() {
        return forecastCacheService.getProductionConsumptionCache();
    }
}
