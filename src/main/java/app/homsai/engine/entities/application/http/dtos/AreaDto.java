package app.homsai.engine.entities.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by Giacomo Agostini on 17/01/17.
 */

public class AreaDto {

    private String uuid;

    @JsonProperty("name")
    private String name;


    @JsonProperty("desired_summer_temperature")
    private Double desiredSummerTemperature;

    @JsonProperty("desired_winter_temperature")
    private Double desiredWinterTemperature;

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

    public Double getDesiredSummerTemperature() {
        return desiredSummerTemperature;
    }

    public void setDesiredSummerTemperature(Double desiredSummerTemperature) {
        this.desiredSummerTemperature = desiredSummerTemperature;
    }

    public Double getDesiredWinterTemperature() {
        return desiredWinterTemperature;
    }

    public void setDesiredWinterTemperature(Double desiredWinterTemperature) {
        this.desiredWinterTemperature = desiredWinterTemperature;
    }
}

