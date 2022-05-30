package app.homsai.engine.entities.application.http.dtos;

import app.homsai.engine.common.domain.models.BaseEntity;
import app.homsai.engine.entities.domain.models.Area;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

public class HAEntityDto {

    private String uuid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("entity_id")
    private String entityId;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("unit_of_measurement")
    private String unitOfMeasurement;

    private List<AreaDto> areas;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<AreaDto> getAreas() {
        return areas;
    }

    public void setAreas(List<AreaDto> areas) {
        this.areas = areas;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }
}

