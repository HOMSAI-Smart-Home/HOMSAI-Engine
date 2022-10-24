package app.homsai.engine.pvoptimizer.application.http.dtos;

import app.homsai.engine.entities.application.http.dtos.AreaDto;
import app.homsai.engine.pvoptimizer.domain.models.HvacDeviceInterval;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

public class HVACDeviceDto {

    @JsonProperty("entity_id")
    private String entityId;

    @JsonProperty("power_consumption")
    private Double powerConsumption;

    @JsonProperty("coupled_power_consumption")
    private Double coupledPowerConsumption;

    @JsonProperty("area")
    private AreaDto area;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("hvac_modes")
    public List<String> hvacModes;

    @JsonProperty("intervals")
    public List<HvacDeviceInterval> intervals;

    @JsonProperty("min_temp")
    private Double minTemp;

    @JsonProperty("max_temp")
    private Double maxTemp;

    @JsonProperty("enabled")
    private Boolean enabled;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Double getPowerConsumption() {
        if(this.powerConsumption == null) return null;
        return (double)Math.round(this.powerConsumption * 100d) / 100d;
    }

    public void setPowerConsumption(Double powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public Double getCoupledPowerConsumption() {
        if(this.coupledPowerConsumption == null) return null;
        return (double)Math.round(this.coupledPowerConsumption * 100d) / 100d;
    }

    public void setCoupledPowerConsumption(Double coupledPowerConsumption) {
        this.coupledPowerConsumption = coupledPowerConsumption;
    }

    public AreaDto getArea() {
        return area;
    }

    public void setArea(AreaDto area) {
        this.area = area;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<String> getHvacModes() {
        return hvacModes;
    }

    public void setHvacModes(List<String> hvacModes) {
        this.hvacModes = hvacModes;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<HvacDeviceInterval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<HvacDeviceInterval> intervals) {
        this.intervals = intervals;
    }
}

