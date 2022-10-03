package app.homsai.engine.forecast.application.http.converters;

import app.homsai.engine.forecast.domain.models.HistoricalSensorState;
import app.homsai.engine.forecast.domain.models.PVBalance;
import app.homsai.engine.forecast.gateways.dtos.PVBalanceDto;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantHistoryDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Giacomo Agostini on 03/10/2022
 */

@Service
public class ForecastMapperImpl implements ForecastMapper{

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PVBalance convertFromDto(PVBalanceDto pvBalanceDto) {
        return modelMapper.map(pvBalanceDto, PVBalance.class);
    }

    @Override
    public List<HistoricalSensorState> convertFromHomeAssistantHistoryDto(List<HomeAssistantHistoryDto> optimizedGeneralPowerMeterData) {
        if(optimizedGeneralPowerMeterData == null)
            return null;
        return optimizedGeneralPowerMeterData.stream()
                .map(homeAssistantHistoryDto -> modelMapper.map(homeAssistantHistoryDto, HistoricalSensorState.class))
                .collect(Collectors.toList());
    }
}
