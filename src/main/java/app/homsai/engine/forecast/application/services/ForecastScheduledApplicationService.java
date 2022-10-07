package app.homsai.engine.forecast.application.services;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Giacomo Agostini on 03/10/2022
 */
public interface ForecastScheduledApplicationService {
    @Scheduled(fixedRate = 5*60*1000)
    void syncHomsaiOptimizationForecast();

    @Scheduled(cron = "0 1 0 * * ?")
    void syncHomsaiProductionConsumptionForecast();
}
