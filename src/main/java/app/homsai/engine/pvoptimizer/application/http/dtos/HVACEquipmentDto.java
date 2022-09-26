package app.homsai.engine.pvoptimizer.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by Giacomo Agostini on 17/01/17.
 */

public class HVACEquipmentDto {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("season")
    private Integer season;

    @JsonProperty("minimum_idle_minutes")
    private Integer minimumIdleMinutes;

    @JsonProperty("minimum_execution_minutes")
    private Integer minimumExecutionMinutes;

    @JsonProperty("switch_required")
    private Boolean switchRequired;

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

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getMinimumIdleMinutes() {
        return minimumIdleMinutes;
    }

    public void setMinimumIdleMinutes(Integer minimumIdleMinutes) {
        this.minimumIdleMinutes = minimumIdleMinutes;
    }

    public Integer getMinimumExecutionMinutes() {
        return minimumExecutionMinutes;
    }

    public void setMinimumExecutionMinutes(Integer minimumExecutionMinutes) {
        this.minimumExecutionMinutes = minimumExecutionMinutes;
    }

    public Boolean getSwitchRequired() {
        return switchRequired;
    }

    public void setSwitchRequired(Boolean switchRequired) {
        this.switchRequired = switchRequired;
    }
}

