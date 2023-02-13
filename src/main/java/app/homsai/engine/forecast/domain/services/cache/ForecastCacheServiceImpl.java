package app.homsai.engine.forecast.domain.services.cache;

import app.homsai.engine.common.domain.utils.constants.Consts;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.forecast.application.http.converters.ForecastMapper;
import app.homsai.engine.forecast.domain.models.PVOptimizationForecastCache;
import app.homsai.engine.forecast.domain.models.ProductionConsumptionCache;
import app.homsai.engine.forecast.gateways.ForecastHomsaiAIServiceGateway;
import app.homsai.engine.forecast.gateways.dtos.*;
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

    private ProductionConsumptionCache productionConsumptionCache;

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

    private ProductionConsumptionCache getProductionConsumptionCacheInstance() {
        if(productionConsumptionCache == null)
            productionConsumptionCache = new ProductionConsumptionCache();
        return productionConsumptionCache;
    }


    @Override
    public PVOptimizationForecastCache getPvOptimizationForecastCache() {
        PVOptimizationForecastCache pvOptimizationForecastCache = getPvOptimizationForecastCacheInstance();
        if(pvOptimizationForecastCache.getDate() == null || !pvOptimizationForecastCache.getDate().equals(Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS))))
            syncHomsaiOptimizationForecast();
        return pvOptimizationForecastCache;
    }

    @Override
    public ProductionConsumptionCache getProductionConsumptionCache() {
        ProductionConsumptionCache productionConsumptionCache = getProductionConsumptionCacheInstance();
        if(productionConsumptionCache.getDate() == null || !productionConsumptionCache.getDate().equals(Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS)))) {
            syncHomsaiProductionConsumptionForecast();
            syncHomeAssistantProductionConsumptionHistorical();
        }
        return productionConsumptionCache;
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
        pvOptimizationForecastCache.setWithHomsai(forecastMapper.convertFromDto(selfConsumptionOptimizationsForecastQueriesDto.getWithHomsai()));
        pvOptimizationForecastCache.setWithoutHomsai(forecastMapper.convertFromDto(selfConsumptionOptimizationsForecastQueriesDto.getWithoutHomsai()));
        pvOptimizationForecastCache.setGlobalConsumptionHistoricalStates(forecastMapper.convertFromHomeAssistantHistoryDto(generalConsumptionHistory));
        pvOptimizationForecastCache.setGlobalConsumptionOptimizedStates(forecastMapper.convertFromHomeAssistantHistoryDto(selfConsumptionOptimizationsForecastQueriesDto.getOptimizedGeneralPowerMeterData()));
        pvOptimizationForecastCache.setStorageHistoricalStates(forecastMapper.convertFromHomeAssistantHistoryDto(pvStorageHistory));
        pvOptimizationForecastCache.setStorageOptimizedStates(forecastMapper.convertFromHomeAssistantHistoryDto(selfConsumptionOptimizationsForecastQueriesDto.getOptimizedBatteryData()));
        pvOptimizationForecastCache.setPvProductionHistoricalStates(forecastMapper.convertFromHomeAssistantHistoryDto(pvProductionHistory));
        pvOptimizationForecastCache.setDate(Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS)));

    }

    @Override
    public void syncHomsaiProductionConsumptionForecast() {
        Integer forecastDays = 1;
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        Instant pvInstallDate = homeInfo.getPvInstallDate();
        Double kwp = homeInfo.getPvPeakPower();
        Double lat = homeInfo.getLatitude();
        Double lng = homeInfo.getLongitude();
        String generalPowerMeterId = homeInfo.getGeneralPowerMeterId();
        if(pvInstallDate == null || kwp == null || lat == null || lng == null || generalPowerMeterId == null)
            return;

        long plantLifeDays = ChronoUnit.DAYS.between(pvInstallDate, Instant.now());
        PVForecastRequestDto pvForecastRequestDto = new PVForecastRequestDto();
        pvForecastRequestDto.setkWp(kwp);
        pvForecastRequestDto.setLat(lat);
        pvForecastRequestDto.setLng(lng);
        pvForecastRequestDto.setDays(forecastDays);
        pvForecastRequestDto.setPlantLifeDays((int) plantLifeDays);
        List<PVForecastQueriesDto> pvForecast = forecastHomsaiAIServiceGateway.getPhotovoltaicProductionForecast(pvForecastRequestDto);
        getProductionConsumptionCacheInstance().setPvProductionForecastStates(forecastMapper.convertToHistoricalSensorState(pvForecast));

        Instant endDate = Instant.now().truncatedTo(ChronoUnit.DAYS).minusSeconds(OffsetDateTime.now().getOffset().getTotalSeconds());
        Instant startDate = endDate.minus(7, ChronoUnit.DAYS);
        List<HomeAssistantHistoryDto> generalConsumptionHistory = homeAssistantQueriesApplicationService.getHomeAssistantHistoryState(startDate, endDate, generalPowerMeterId);
        generalConsumptionHistory.get(0).setLastChanged(startDate);
        generalConsumptionHistory.get(0).setLastUpdated(startDate);
        ConsumptionForecastRequestDto consumptionForecastRequestDto = new ConsumptionForecastRequestDto();
        consumptionForecastRequestDto.setConsumptionMeterData(generalConsumptionHistory);
        List<HomeAssistantHistoryDto> consumptionForecast = forecastHomsaiAIServiceGateway.getConsumptionForecast(consumptionForecastRequestDto, forecastDays);
        getProductionConsumptionCacheInstance().setConsumptionForecastStates(forecastMapper.convertFromHomeAssistantHistoryDto(consumptionForecast));

        getProductionConsumptionCacheInstance().setDate(Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS)));

    }

    @Override
    public void syncHomeAssistantProductionConsumptionHistorical() {
        Instant startDate = Instant.now().truncatedTo(ChronoUnit.DAYS).minusSeconds(OffsetDateTime.now().getOffset().getTotalSeconds());
        Instant endDate = null;//Instant.now().minusSeconds(OffsetDateTime.now().getOffset().getTotalSeconds());
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        String pvProductionSensorId = homeInfo.getPvProductionSensorId();
        String generalPowerMeterId = homeInfo.getGeneralPowerMeterId();
        if(pvProductionSensorId == null || generalPowerMeterId == null)
            return;
        List<HomeAssistantHistoryDto> pvProductionHistoricalStates = homeAssistantQueriesApplicationService.getHomeAssistantHistoryState(startDate, endDate, pvProductionSensorId);
        List<HomeAssistantHistoryDto> consumptionHistoricalStates = homeAssistantQueriesApplicationService.getHomeAssistantHistoryState(startDate, endDate, generalPowerMeterId);
        getProductionConsumptionCacheInstance().setPvProductionHistoricalStates(forecastMapper.convertFromHomeAssistantHistoryDtoToHistoricalPower(pvProductionHistoricalStates));
        getProductionConsumptionCacheInstance().getPvProductionHistoricalStates().get(0).setLastChanged(startDate);
        getProductionConsumptionCacheInstance().getPvProductionHistoricalStates().get(0).setLastUpdated(startDate.toString());
        getProductionConsumptionCacheInstance().setConsumptionHistoricalStates(forecastMapper.convertFromHomeAssistantHistoryDtoToHistoricalPower(consumptionHistoricalStates));
        getProductionConsumptionCacheInstance().getConsumptionHistoricalStates().get(0).setLastChanged(startDate);
        getProductionConsumptionCacheInstance().getConsumptionHistoricalStates().get(0).setLastUpdated(startDate.toString());
    }


}
