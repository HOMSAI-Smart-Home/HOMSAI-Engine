package app.homsai.engine.forecast.gateways.dtos;

import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantHistoryDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SelfConsumptionOptimizationsForecastRequestDto {

    @JsonProperty("general_power_meter_data")
    List<HomeAssistantHistoryDto> generalPowerMeterHistoricalData;

    @JsonProperty("pv_production_meter_data")
    List<HomeAssistantHistoryDto> pvProductionPowerMeterHistoricalData;

    @JsonProperty("battery_meter_data")
    List<HomeAssistantHistoryDto> batteryPowerMeterHistoricalData;

    public List<HomeAssistantHistoryDto> getGeneralPowerMeterHistoricalData() {
        return generalPowerMeterHistoricalData;
    }

    public void setGeneralPowerMeterHistoricalData(List<HomeAssistantHistoryDto> generalPowerMeterHistoricalData) {
        this.generalPowerMeterHistoricalData = generalPowerMeterHistoricalData;
    }

    public List<HomeAssistantHistoryDto> getPvProductionPowerMeterHistoricalData() {
        return pvProductionPowerMeterHistoricalData;
    }

    public List<HomeAssistantHistoryDto> getBatteryPowerMeterHistoricalData() {
        return batteryPowerMeterHistoricalData;
    }

    public void setBatteryPowerMeterHistoricalData(List<HomeAssistantHistoryDto> batteryPowerMeterHistoricalData) {
        this.batteryPowerMeterHistoricalData = batteryPowerMeterHistoricalData;
    }

    public void setPvProductionPowerMeterHistoricalData(List<HomeAssistantHistoryDto> pvProductionPowerMeterHistoricalData) {
        this.pvProductionPowerMeterHistoricalData = pvProductionPowerMeterHistoricalData;


    }
}
