package app.homsai.engine.entities.application.http.dtos;

import app.homsai.engine.common.domain.models.BaseEntity;
import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.models.HomsaiEntityType;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */


public class HomsaiEntityDto {



    @JsonProperty("name")
    private String name;

    @JsonProperty("unit_of_measurement")
    private String unitOfMeasurement;

    @JsonProperty("area")
    private AreaDto area;

    @JsonProperty("type")
    private HomsaiEntityTypeDto type;

    @JsonProperty("home_assistant_entities")
    private Collection<HAEntityDto> haEntities;

    public HomsaiEntityDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public AreaDto getArea() {
        return area;
    }

    public void setArea(AreaDto area) {
        this.area = area;
    }

    public HomsaiEntityTypeDto getType() {
        return type;
    }

    public void setType(HomsaiEntityTypeDto type) {
        this.type = type;
    }

    public Collection<HAEntityDto> getHaEntities() {
        return haEntities;
    }

    public void setHaEntities(Collection<HAEntityDto> haEntities) {
        this.haEntities = haEntities;
    }
}

