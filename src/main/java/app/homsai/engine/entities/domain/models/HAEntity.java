package app.homsai.engine.entities.domain.models;

import app.homsai.engine.common.domain.models.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Entity
@Table(name = "entities")
public class HAEntity extends BaseEntity {

    @NotNull
    @Column(length = 255, name = "name")
    private String name;

    @NotNull
    @Column(name = "entity_id")
    private String entityId;

    @NotNull
    @Column(name = "domain")
    private String domain;

    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "entities_areas",
            joinColumns = @JoinColumn(name = "entity_uuid", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "area_uuid",
                    referencedColumnName = "uuid"))
    private Collection<Area> areas;

    public HAEntity(String name, String entityId, String domain) {
        this.name = name;
        this.entityId = entityId;
        this.domain = domain;
    }

    public HAEntity(String name, String entityId, String domain, String unitOfMeasurement) {
        this.name = name;
        this.entityId = entityId;
        this.domain = domain;
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public HAEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Area> getAreas() {
        return areas;
    }

    public void setAreas(Collection<Area> areas) {
        this.areas = areas;
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

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }
}

