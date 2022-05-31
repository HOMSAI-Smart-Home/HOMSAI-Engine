package app.homsai.engine.entities.domain.models;

import app.homsai.engine.common.domain.models.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Entity
@Table(name = "homsai_entities")
public class HomsaiEntity extends BaseEntity {

    @NotNull
    @Column(length = 255, name = "name")
    private String name;

    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement;

    @ManyToOne
    @JoinColumn(name = "area_uuid")
    private Area area;

    @ManyToOne
    @JoinColumn(name = "type_uuid")
    private HomsaiEntityType type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ha_entities_homsai_entities",
            joinColumns = @JoinColumn(name = "homsai_entiity_uuid", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "ha_entity_uuid",
                    referencedColumnName = "uuid"))
    private Collection<HAEntity> haEntities;

    public HomsaiEntity() {
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

    public Collection<HAEntity> getHaEntities() {
        return haEntities;
    }

    public void setHaEntities(Collection<HAEntity> haEntities) {
        this.haEntities = haEntities;
    }
}

