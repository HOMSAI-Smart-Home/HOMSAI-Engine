package app.homsai.engine.forecast.application.http.converters;

import app.homsai.engine.forecast.domain.models.HistoricalSensorState;
import app.homsai.engine.forecast.domain.models.PVBalance;
import app.homsai.engine.forecast.gateways.dtos.PVBalanceDto;
import app.homsai.engine.forecast.gateways.dtos.PVForecastQueriesDto;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantHistoryDto;

import java.util.List;

/**
 * @author Giacomo Agostini on 03/10/2022
 */
public interface ForecastMapper {
    PVBalance convertFromDto(PVBalanceDto pvBalanceDto);

    List<HistoricalSensorState> convertFromHomeAssistantHistoryDto(List<HomeAssistantHistoryDto> optimizedGeneralPowerMeterData);

    List<HistoricalSensorState> convertToHistoricalSensorState(List<PVForecastQueriesDto> pvForecast);
}
