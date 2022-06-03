package app.homsai.engine.entities.domain.models;

import app.homsai.engine.common.domain.models.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Entity
@Table(name = "homsai_entity_types")
public class HomsaiEntityType extends BaseEntity {

    @NotNull
    @Column(length = 255, name = "name")
    private String name;

    @NotNull
    @Column(length = 255, name = "root_name")
    private String rootName;

    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement;

    @Column(name = "device_class")
    private String deviceClass;

    @Column(name = "operator")
    private String operator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public String getDeviceClass() {
        return deviceClass;
    }

    public void setDeviceClass(String deviceClass) {
        this.deviceClass = deviceClass;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}

