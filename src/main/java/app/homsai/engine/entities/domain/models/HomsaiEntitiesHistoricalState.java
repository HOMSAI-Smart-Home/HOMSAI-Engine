package app.homsai.engine.entities.domain.models;

import app.homsai.engine.common.domain.models.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Entity
@Table(name = "homsai_entities_historical_states")
public class HomsaiEntitiesHistoricalState extends BaseEntity {

    @Column
    private Instant timestamp;

    @Column(name = "value")
    private Double value;


    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement;

    @ManyToOne
    @JoinColumn(name = "area_uuid")
    private Area area;

    @ManyToOne
    @JoinColumn(name = "type_uuid")
    private HomsaiEntityType type;

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public HomsaiEntityType getType() {
        return type;
    }

    public void setType(HomsaiEntityType type) {
        this.type = type;
    }
}

