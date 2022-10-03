package app.homsai.engine.forecast.domain.models;

import java.util.Date;
import java.util.List;

/**
 * @author Giacomo Agostini on 03/10/2022
 */
public class PVOptimizationForecastCache {

    private Date date;

    private List<HistoricalSensorState> pvProductionHistoricalStates;

    private List<HistoricalSensorState> globalConsumptionHistoricalStates;

    private List<HistoricalSensorState> globalConsumptionOptimizedStates;

    private List<HistoricalSensorState> storageHistoricalStates;

    private List<HistoricalSensorState> storageOptimizedStates;

    private PVBalance withHomsai;

    private PVBalance withoutHomsai;

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

    public List<HistoricalSensorState> getGlobalConsumptionHistoricalStates() {
        return globalConsumptionHistoricalStates;
    }

    public void setGlobalConsumptionHistoricalStates(List<HistoricalSensorState> globalConsumptionHistoricalStates) {
        this.globalConsumptionHistoricalStates = globalConsumptionHistoricalStates;
    }

    public List<HistoricalSensorState> getGlobalConsumptionOptimizedStates() {
        return globalConsumptionOptimizedStates;
    }

    public void setGlobalConsumptionOptimizedStates(List<HistoricalSensorState> globalConsumptionOptimizedStates) {
        this.globalConsumptionOptimizedStates = globalConsumptionOptimizedStates;
    }

    public List<HistoricalSensorState> getStorageHistoricalStates() {
        return storageHistoricalStates;
    }

    public void setStorageHistoricalStates(List<HistoricalSensorState> storageHistoricalStates) {
        this.storageHistoricalStates = storageHistoricalStates;
    }

    public List<HistoricalSensorState> getStorageOptimizedStates() {
        return storageOptimizedStates;
    }

    public void setStorageOptimizedStates(List<HistoricalSensorState> storageOptimizedStates) {
        this.storageOptimizedStates = storageOptimizedStates;
    }

    public PVBalance getWithHomsai() {
        return withHomsai;
    }

    public void setWithHomsai(PVBalance withHomsai) {
        this.withHomsai = withHomsai;
    }

    public PVBalance getWithoutHomsai() {
        return withoutHomsai;
    }

    public void setWithoutHomsai(PVBalance withoutHomsai) {
        this.withoutHomsai = withoutHomsai;
    }
}
