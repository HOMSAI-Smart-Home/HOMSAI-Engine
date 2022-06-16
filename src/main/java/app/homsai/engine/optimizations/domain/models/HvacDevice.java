package app.homsai.engine.optimizations.domain.models;


import app.homsai.engine.common.domain.utils.Consts;

import java.time.Instant;
import java.util.List;

public class HvacDevice {

    private String entityId;

    private Boolean active;

    private String areaId;

    private Double currentTemperature;

    private Double setTemperature;

    private Double deltaTemperature;

    private List<HvacInterval> intervals;

    private Double powerConsumption;

    private Double actualPowerConsumption;

    private Instant startTime;

    private Instant endTime;

    private boolean enabled;

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
        if( Consts.HVAC_MODE.equals("summer"))
            deltaTemperature = currentTemperature - setTemperature;
        else
            deltaTemperature = setTemperature - currentTemperature;

    }

    public List<HvacInterval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<HvacInterval> intervals) {
        this.intervals = intervals;
    }

    public Double getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(Double powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public Double getActualPowerConsumption() {
        return actualPowerConsumption;
    }

    public void setActualPowerConsumption(Double actualPowerConsumption) {
        this.actualPowerConsumption = actualPowerConsumption;
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
}
