package app.homsai.engine.forecast.gateways.dtos;

import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantHistoryDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SelfConsumptionOptimizationsForecastQueriesDto {

    @JsonProperty("optimized_general_power_meter_data")
    private List<HomeAssistantHistoryDto> optimizedGeneralPowerMeterData;

    @JsonProperty("optimized_battery_data")
    private List<HomeAssistantHistoryDto> optimizedBatteryData;

    @JsonProperty("without_homsai")
    private PVBalanceDto withoutHomsai;

    @JsonProperty("with_homsai")
    private PVBalanceDto withHomsai;

    public List<HomeAssistantHistoryDto> getOptimizedGeneralPowerMeterData() {
        return optimizedGeneralPowerMeterData;
    }

    public void setOptimizedGeneralPowerMeterData(List<HomeAssistantHistoryDto> optimizedGeneralPowerMeterData) {
        this.optimizedGeneralPowerMeterData = optimizedGeneralPowerMeterData;
    }

    public PVBalanceDto getWithoutHomsai() {
        return withoutHomsai;
    }

    public void setWithoutHomsai(PVBalanceDto withoutHomsai) {
        this.withoutHomsai = withoutHomsai;
    }

    public PVBalanceDto getWithHomsai() {
        return withHomsai;
    }

    public void setWithHomsai(PVBalanceDto withHomsai) {
        this.withHomsai = withHomsai;
    }

    public List<HomeAssistantHistoryDto> getOptimizedBatteryData() {
        return optimizedBatteryData;
    }

    public void setOptimizedBatteryData(List<HomeAssistantHistoryDto> optimizedBatteryData) {
        this.optimizedBatteryData = optimizedBatteryData;
    }
}
