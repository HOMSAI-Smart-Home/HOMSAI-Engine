package app.homsai.engine.pvoptimizer.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

public class HvacDeviceSettingDto {

    private Boolean enabled;

    private Boolean manual;

    @JsonProperty("set_temperature")
    private Double setTemperature;

    @JsonProperty("start_time")
    private LocalTime startTime;

    @JsonProperty("end_time")
    private LocalTime endTime;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    public Double getSetTemperature() {
        return setTemperature;
    }

    public void setSetTemperature(Double setTemperature) {
        this.setTemperature = setTemperature;
    }

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
