package app.homsai.engine.forecast.domain.services.cache;

import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.forecast.application.http.converters.ForecastMapper;
import app.homsai.engine.forecast.domain.models.PVOptimizationForecastCache;
import app.homsai.engine.forecast.domain.services.ForecastCommandsService;
import app.homsai.engine.forecast.gateways.ForecastHomsaiAIServiceGateway;
import app.homsai.engine.forecast.gateways.dtos.SelfConsumptionOptimizationsForecastQueriesDto;
import app.homsai.engine.forecast.gateways.dtos.SelfConsumptionOptimizationsForecastRequestDto;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author Giacomo Agostini on 03/10/2022
 */

@Service
public class ForecastCacheServiceImpl implements ForecastCacheService{

    private PVOptimizationForecastCache pvOptimizationForecastCache;

    @Autowired
    HomeAssistantQueriesApplicationService homeAssistantQueriesApplicationService;

    @Autowired
    EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    @Autowired
    ForecastHomsaiAIServiceGateway forecastHomsaiAIServiceGateway;

    @Autowired
    ForecastMapper forecastMapper;

    private PVOptimizationForecastCache getPvOptimizationForecastCacheInstance() {
        if(pvOptimizationForecastCache == null)
            pvOptimizationForecastCache = new PVOptimizationForecastCache();
        return pvOptimizationForecastCache;
    }

    @Override
    public PVOptimizationForecastCache getPvOptimizationForecastCache() {
        PVOptimizationForecastCache pvOptimizationForecastCache = getPvOptimizationForecastCacheInstance();
        if(pvOptimizationForecastCache.getDate() == null || !pvOptimizationForecastCache.getDate().equals(Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS))))
            syncHomsaiOptimizationForecast();
        return pvOptimizationForecastCache;
    }

    @Override
    public void syncHomsaiOptimizationForecast(){
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        String currency = Consts.HOME_ASSISTANT_EUR_UNIT.equals(homeAssistantQueriesApplicationService.getConfig().getCurrency()) ?
                Consts.EUR_UNIT :
                Consts.US_UNIT;
        String generalPowerMeterId = homeInfo.getGeneralPowerMeterId();
        String pvProductionSensorId = homeInfo.getPvProductionSensorId();
        String pvStorageSensorId = homeInfo.getPvStorageSensorId();
        Instant endDate = Instant.now().truncatedTo(ChronoUnit.DAYS).minusSeconds(OffsetDateTime.now().getOffset().getTotalSeconds());
        Instant startDate = endDate.minus(1, ChronoUnit.DAYS);
        List<HomeAssistantHistoryDto> generalConsumptionHistory = homeAssistantQueriesApplicationService.getHomeAssistantHistoryState(startDate, endDate, generalPowerMeterId);
        List<HomeAssistantHistoryDto> pvProductionHistory = homeAssistantQueriesApplicationService.getHomeAssistantHistoryState(startDate, endDate, pvProductionSensorId);
        List<HomeAssistantHistoryDto> pvStorageHistory = pvStorageSensorId != null ?
                homeAssistantQueriesApplicationService.getHomeAssistantHistoryState(startDate, endDate, pvStorageSensorId) :
                null;
        SelfConsumptionOptimizationsForecastRequestDto selfConsumptionOptimizationsForecastRequestDto = new SelfConsumptionOptimizationsForecastRequestDto();
        selfConsumptionOptimizationsForecastRequestDto.setGeneralPowerMeterHistoricalData(generalConsumptionHistory);
        selfConsumptionOptimizationsForecastRequestDto.setPvProductionPowerMeterHistoricalData(pvProductionHistory);
        selfConsumptionOptimizationsForecastRequestDto.setBatteryPowerMeterHistoricalData(pvStorageHistory);
        SelfConsumptionOptimizationsForecastQueriesDto selfConsumptionOptimizationsForecastQueriesDto = forecastHomsaiAIServiceGateway.getPhotovoltaicSelfConsumptionOptimizerForecast(selfConsumptionOptimizationsForecastRequestDto, currency);
        PVOptimizationForecastCache pvOptimizationForecastCache = getPvOptimizationForecastCacheInstance();
        pvOptimizationForecastCache.setDate(Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS)));
        pvOptimizationForecastCache.setWithHomsai(forecastMapper.convertFromDto(selfConsumptionOptimizationsForecastQueriesDto.getWithHomsai()));
        pvOptimizationForecastCache.setWithoutHomsai(forecastMapper.convertFromDto(selfConsumptionOptimizationsForecastQueriesDto.getWithoutHomsai()));
        pvOptimizationForecastCache.setGlobalConsumptionHistoricalStates(forecastMapper.convertFromHomeAssistantHistoryDto(generalConsumptionHistory));
        pvOptimizationForecastCache.setGlobalConsumptionOptimizedStates(forecastMapper.convertFromHomeAssistantHistoryDto(selfConsumptionOptimizationsForecastQueriesDto.getOptimizedGeneralPowerMeterData()));
        pvOptimizationForecastCache.setStorageHistoricalStates(forecastMapper.convertFromHomeAssistantHistoryDto(pvStorageHistory));
        pvOptimizationForecastCache.setStorageOptimizedStates(forecastMapper.convertFromHomeAssistantHistoryDto(selfConsumptionOptimizationsForecastQueriesDto.getOptimizedBatteryData()));
        pvOptimizationForecastCache.setPvProductionHistoricalStates(forecastMapper.convertFromHomeAssistantHistoryDto(pvProductionHistory));
    }


}
