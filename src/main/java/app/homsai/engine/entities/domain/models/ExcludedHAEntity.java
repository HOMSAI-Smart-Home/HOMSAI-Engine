package app.homsai.engine.entities.domain.models;

import app.homsai.engine.common.domain.models.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Entity
@Table(name = "ha_excluded_entities")
public class ExcludedHAEntity extends BaseEntity {

    @NotNull
    @Column(length = 255, name = "entity_id")
    private String entityId;

    public ExcludedHAEntity(String entityId) {
        this.entityId = entityId;
    }

    public ExcludedHAEntity() {
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}

