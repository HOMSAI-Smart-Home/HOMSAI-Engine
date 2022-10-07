package app.homsai.engine.forecast.gateways;

import app.homsai.engine.forecast.gateways.dtos.*;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantHistoryDto;

import java.util.List;

public interface ForecastHomsaiAIServiceGateway {

    SelfConsumptionOptimizationsForecastQueriesDto getPhotovoltaicSelfConsumptionOptimizerForecast(SelfConsumptionOptimizationsForecastRequestDto selfConsumptionOptimizationsForecastRequestDto, String currency);

    List<PVForecastQueriesDto> getPhotovoltaicProductionForecast(PVForecastRequestDto pvForecastRequestDto);

    List<HomeAssistantHistoryDto> getConsumptionForecast(ConsumptionForecastRequestDto consumptionForecastRequestDto, Integer days);
}
