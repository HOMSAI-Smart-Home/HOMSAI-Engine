package app.homsai.engine.entities.application.http.dtos;

import app.homsai.engine.common.domain.models.BaseEntity;
import app.homsai.engine.entities.domain.models.HAEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

public class AreaDto {

    private String uuid;

    @JsonProperty("name")
    private String name;

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
}

