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
@Table(name = "ha_areas")
public class Area extends BaseEntity {

    @NotNull
    @Column(length = 255, name = "name")
    private String name;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "entities_areas",
            joinColumns = @JoinColumn(name = "area_uuid", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "entity_uuid",
                    referencedColumnName = "uuid"))
    private Collection<HAEntity> entities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<HAEntity> getEntities() {
        return entities;
    }

    public void setEntities(Collection<HAEntity> entities) {
        this.entities = entities;
    }


}

