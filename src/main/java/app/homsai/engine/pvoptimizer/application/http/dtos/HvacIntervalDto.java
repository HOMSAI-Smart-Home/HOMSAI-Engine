package app.homsai.engine.pvoptimizer.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

public class HvacIntervalDto {

    @JsonProperty("start_time")
    private LocalTime startTime;

    @JsonProperty("end_time")
    private LocalTime endTime;

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
