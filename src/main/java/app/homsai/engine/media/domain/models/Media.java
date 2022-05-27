package app.homsai.engine.media.domain.models;

import app.homsai.engine.common.domain.models.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Entity
@Table(name = "media")
public class Media extends BaseEntity {

    @NotNull
    @Column(length = 255, name = "media_tag")
    private String tag;

    @Column(length = 255, name = "custom_tag")
    private String customTag;

    @NotNull
    @Column(length = 255)
    private String mimetype;

    @NotNull
    @Column(length = 255, name = "file_name")
    private String filename;

    @NotNull
    @Column(length = 255, name = "original_extension")
    private String originalExtension;

    @Column(length = 255, name = "original_file_name")
    private String originalFileName;

    @NotNull
    @Column(name = "media_size")
    private Long size;

    @Column(name = "media_type")
    private Integer type;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MediaEntity> mediaEntity;



    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getOriginalExtension() {
        return originalExtension;
    }

    public void setOriginalExtension(String originalExtension) {
        this.originalExtension = originalExtension;
    }

    public String getCustomTag() {
        return customTag;
    }

    public void setCustomTag(String customTag) {
        this.customTag = customTag;
    }

    public Set<MediaEntity> getMediaEntity() {
        return mediaEntity;
    }

    public void setMediaEntity(Set<MediaEntity> mediaEntity) {
        this.mediaEntity = mediaEntity;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

