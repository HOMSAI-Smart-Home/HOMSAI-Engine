package app.homsai.engine.pvoptimizer.domain.models;

import app.homsai.engine.common.domain.models.BaseEntity;
import app.homsai.engine.entities.domain.models.Area;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Entity
@Table(name = "hvac_devices")
public class HVACDevice extends BaseEntity {

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "power_consumption")
    private Double powerConsumption;

    @Column(name = "coupled_power_consumption")
    private Double coupledPowerConsumption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_uuid")
    private Area area;

    @Column(name = "type")
    private Integer type;

    @ElementCollection
    @CollectionTable(name="hvac_modes", joinColumns=@JoinColumn(name="hvac_device_uuid"))
    @Column(name="hvac_modes")
    public List<String> hvacModes;

    @ElementCollection
    @CollectionTable(name="hvac_device_intervals", joinColumns=@JoinColumn(name="hvac_device_uuid"))
    public List<HvacDeviceInterval> intervals;

    @Column(name = "min_temp")
    private Double minTemp;

    @Column(name = "max_temp")
    private Double maxTemp;

    @Column(name="enabled")
    private Boolean enabled;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Double getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(Double powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public Double getCoupledPowerConsumption() {
        return coupledPowerConsumption;
    }

    public void setCoupledPowerConsumption(Double coupledPowerConsumption) {
        this.coupledPowerConsumption = coupledPowerConsumption;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
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

