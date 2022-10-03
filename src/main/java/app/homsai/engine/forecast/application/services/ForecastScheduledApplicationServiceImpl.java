package app.homsai.engine.forecast.application.services;

import app.homsai.engine.forecast.domain.services.cache.ForecastCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Giacomo Agostini on 03/10/2022
 */

@Service
public class ForecastScheduledApplicationServiceImpl implements ForecastScheduledApplicationService{

    @Autowired
    ForecastCacheService forecastCacheService;


    @Override
    @Scheduled(cron = "0 1 0 * * ?")
    public void syncHomsaiOptimizationForecast(){
        forecastCacheService.syncHomsaiOptimizationForecast();
    }
}
