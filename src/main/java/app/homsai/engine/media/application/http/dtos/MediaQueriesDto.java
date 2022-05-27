package app.homsai.engine.media.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import app.homsai.engine.common.application.http.dtos.BaseDto;


/**
 * Created by Giacomo Agostini on 02/12/16.
 *
 */
public class MediaQueriesDto extends BaseDto {

    private String uuid;

    private String tag;

    private String mimetype;

    private String filename;

    private Long size;

    @JsonProperty("original_filename")
    private String originalFileName;

    @JsonProperty("entity_uuid")
    private String entityUuid;

    @JsonProperty("custom_tag")
    private String customTag;

    @JsonProperty("original_extension")
    private String originalExtension;

    @JsonProperty("file_path")
    private String filePath;

    private Integer type;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

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

    public String getEntityUuid() {
        return entityUuid;
    }

    public void setEntityUuid(String entityUuid) {
        this.entityUuid = entityUuid;
    }

    public String getCustomTag() {
        return customTag;
    }

    public void setCustomTag(String customTag) {
        this.customTag = customTag;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
