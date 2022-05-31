package app.homsai.engine.entities.application.http.dtos;

import app.homsai.engine.common.domain.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

public class HomsaiEntityTypeDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("root_name")
    private String rootName;

    @JsonProperty("unit_of_measurement")
    private String unitOfMeasurement;

    @JsonProperty("device_class")
    private String deviceClass;

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
}

