package app.homsai.engine.forecast.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * @author Giacomo Agostini on 06/10/2022
 */
public class ProductionConsumptionCache {

    private Date date;

    @JsonProperty("pv_production_historical_states")
    private List<HistoricalSensorState> pvProductionHistoricalStates;

    @JsonProperty("consumption_historical_states")
    private List<HistoricalSensorState> consumptionHistoricalStates;

    @JsonProperty("pv_production_forecast_states")
    private List<HistoricalSensorState> pvProductionForecastStates;

    @JsonProperty("consumption_forecast_states")
    private List<HistoricalSensorState> consumptionForecastStates;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<HistoricalSensorState> getPvProductionHistoricalStates() {
        return pvProductionHistoricalStates;
    }

    public void setPvProductionHistoricalStates(List<HistoricalSensorState> pvProductionHistoricalStates) {
        this.pvProductionHistoricalStates = pvProductionHistoricalStates;
    }

    public List<HistoricalSensorState> getConsumptionHistoricalStates() {
        return consumptionHistoricalStates;
    }

    public void setConsumptionHistoricalStates(List<HistoricalSensorState> consumptionHistoricalStates) {
        this.consumptionHistoricalStates = consumptionHistoricalStates;
    }

    public List<HistoricalSensorState> getPvProductionForecastStates() {
        return pvProductionForecastStates;
    }

    public void setPvProductionForecastStates(List<HistoricalSensorState> pvProductionForecastStates) {
        this.pvProductionForecastStates = pvProductionForecastStates;
    }

    public List<HistoricalSensorState> getConsumptionForecastStates() {
        return consumptionForecastStates;
    }

    public void setConsumptionForecastStates(List<HistoricalSensorState> consumptionForecastStates) {
        this.consumptionForecastStates = consumptionForecastStates;
    }
}
