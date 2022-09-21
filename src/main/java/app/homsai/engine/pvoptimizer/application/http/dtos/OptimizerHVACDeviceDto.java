package app.homsai.engine.pvoptimizer.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public class OptimizerHVACDeviceDto {

    @JsonProperty("entity_id")
    private String entityId;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("area_id")
    private String areaId;

    @JsonProperty("current_temperature")
    private Double currentTemperature;

    @JsonProperty("set_temperature")
    private Double setTemperature;

    @JsonProperty("delta_temperature")
    private Double deltaTemperature;

    @JsonProperty("intervals")
    private List<HvacIntervalDto> intervals;

    @JsonProperty("power_consumption")
    private Double powerConsumption;

    @JsonProperty("actual_power_consumption")
    private Double actualPowerConsumption;

    @JsonProperty("start_time")
    private Instant startTime;

    @JsonProperty("end_time")
    private Instant endTime;

    @JsonProperty("enabled")
    private Boolean enabled;

    @JsonProperty("manual")
    private boolean manual;

    @JsonProperty("hvac_mode")
    private String hvacMode;

    @JsonProperty("type")
    private Integer type;


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
    }

    public Double getSetTemperature() {
        return setTemperature;
    }

    public void setSetTemperature(Double setTemperature) {
        this.setTemperature = setTemperature;
    }

    public Double getDeltaTemperature() {
        return deltaTemperature;
    }

    public void setDeltaTemperature(Double deltaTemperature) {
        this.deltaTemperature = deltaTemperature;
    }

    public List<HvacIntervalDto> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<HvacIntervalDto> intervals) {
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
