package app.homsai.engine.entities.domain.models;

import app.homsai.engine.common.domain.models.BaseEntity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_uuid")
    private Area area;

    @Column(name = "type")
    private Integer type;

    @ElementCollection
    @CollectionTable(name="hvac_modes", joinColumns=@JoinColumn(name="hvac_device_uuid"))
    @Column(name="hvac_modes")
    public List<String> hvacModes;

    @Column(name = "min_temp")
    private Double minTemp;

    @Column(name = "max_temp")
    private Double maxTemp;

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
}

