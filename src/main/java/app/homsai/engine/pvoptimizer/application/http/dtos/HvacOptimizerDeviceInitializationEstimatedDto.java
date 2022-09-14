package app.homsai.engine.pvoptimizer.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;


public class HvacOptimizerDeviceInitializationEstimatedDto {


    @JsonProperty("total_time_seconds")
    private Integer totalTimeSeconds;

    public Integer getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public void setTotalTimeSeconds(Integer totalTimeSeconds) {
        this.totalTimeSeconds = totalTimeSeconds;
    }
}
