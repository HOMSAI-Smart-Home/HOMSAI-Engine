package app.homsai.engine.pvoptimizer.domain.models;

import app.homsai.engine.common.domain.utils.constants.Consts;

import java.time.Instant;
import java.util.List;

import static app.homsai.engine.common.domain.utils.constants.Consts.HVAC_MODE_SUMMER_ID;

public class OptimizerHVACDevice {

    private String entityId;

    private Boolean active;

    private String areaId;

    private Double currentTemperature;

    private Double setTemperature;

    private Double deltaTemperature;

    private List<OptimizerHVACInterval> intervals;

    private Double powerConsumption;

    private Double coupledPowerConsumption;

    private Double actualPowerConsumption;

    private Instant startTime;

    private Instant endTime;

    private boolean enabled;

    private boolean manual;

    private String hvacMode;

    private String oldHvacMode;

    private Double oldSetTemp;

    private DoubleCircularArray consumptionArray;

    private Integer type;

    public OptimizerHVACDevice() {
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(Double currentTemperature) {
        this.currentTemperature = currentTemperature;
        setDeltaTemperature();
    }

    public Double getSetTemperature() {
        return setTemperature;
    }

    public void setSetTemperature(Double setTemperature) {
        this.setTemperature = setTemperature;
        setDeltaTemperature();
    }

    public Double getDeltaTemperature() {
        return deltaTemperature;
    }

    private void setDeltaTemperature() {
        if(setTemperature == null || currentTemperature == null){
            deltaTemperature = 0D;
            return;
        }
        if(type == HVAC_MODE_SUMMER_ID)
            deltaTemperature = currentTemperature - setTemperature;
        else
            deltaTemperature = setTemperature - currentTemperature;

    }

    public List<OptimizerHVACInterval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<OptimizerHVACInterval> intervals) {
        this.intervals = intervals;
    }

    public Double getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(Double powerConsumption) {
        this.powerConsumption = powerConsumption;
        this.consumptionArray = new DoubleCircularArray(30, powerConsumption);
    }

    public Double getCoupledPowerConsumption() {
        return coupledPowerConsumption;
    }

    public void setCoupledPowerConsumption(Double coupledPowerConsumption) {
        this.coupledPowerConsumption = coupledPowerConsumption;
    }

    public Double getActualPowerConsumption() {
        if(active)
            return this.consumptionArray.getAverageValue();
        else
            return 0D;
    }

    public void setActualPowerConsumption(Double actualPowerConsumption) {
        this.consumptionArray.insert(actualPowerConsumption);
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setDeltaTemperature(Double deltaTemperature) {
        this.deltaTemperature = deltaTemperature;
    }

    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public String getHvacMode() {
        return hvacMode;
    }

    public void setHvacMode(String hvacMode) {
        this.hvacMode = hvacMode;
    }

    public DoubleCircularArray getConsumptionArray() {
        return consumptionArray;
    }

    public void setConsumptionArray(DoubleCircularArray consumptionArray) {
        this.consumptionArray = consumptionArray;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOldHvacMode() {
        if(oldHvacMode != null)
            return oldHvacMode;
        return Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION;
    }

    public void setOldHvacMode(String oldHvacMode) {
        this.oldHvacMode = oldHvacMode;
    }

    public Double getOldSetTemp() {
        if(oldSetTemp != null)
            return oldSetTemp;
        return 22D;
    }

    public void setOldSetTemp(Double oldSetTemp) {
        this.oldSetTemp = oldSetTemp;
    }
}
