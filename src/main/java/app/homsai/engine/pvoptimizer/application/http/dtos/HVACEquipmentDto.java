package app.homsai.engine.pvoptimizer.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

public class HVACEquipmentDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("season")
    private Integer season;

    @JsonProperty("minimum_idle_minutes")
    private Integer minimumIdleMinutes;

    @JsonProperty("minimum_execution_minutes")
    private Integer minimumExecutionMinutes;

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
}

