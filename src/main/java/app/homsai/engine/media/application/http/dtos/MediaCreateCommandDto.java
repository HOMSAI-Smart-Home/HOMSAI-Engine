package app.homsai.engine.media.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */
public class MediaCreateCommandDto {

    private String uuid;

    private String tag;

    private String mimetype;

    private String filename;

    private Long size;

    @JsonProperty("original_filename")
    private String originalFileName;

    @JsonProperty("original_extension")
    private String originalExtension;

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

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
}
