package app.homsai.engine.forecast.gateways.dtos;

import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantHistoryDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ConsumptionForecastRequestDto {

    @JsonProperty("consumption_meter_data")
    List<HomeAssistantHistoryDto> consumptionMeterData;

    public List<HomeAssistantHistoryDto> getConsumptionMeterData() {
        return consumptionMeterData;
    }

    public void setConsumptionMeterData(List<HomeAssistantHistoryDto> consumptionMeterData) {
        this.consumptionMeterData = consumptionMeterData;
    }
}
