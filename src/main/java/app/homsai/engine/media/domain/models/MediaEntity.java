package app.homsai.engine.media.domain.models;

import app.homsai.engine.common.domain.models.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Entity
@Table(name = "media_entity")
public class MediaEntity extends BaseEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "media_uuid")
    private Media media;

    @Column(length = 255, name = "entity_uuid")
    private String entityUuid;

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public String getEntityUuid() {
        return entityUuid;
    }

    public void setEntityUuid(String entityUuid) {
        this.entityUuid = entityUuid;
    }
}
