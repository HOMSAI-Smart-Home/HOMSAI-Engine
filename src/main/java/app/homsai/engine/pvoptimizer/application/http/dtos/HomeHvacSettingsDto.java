package app.homsai.engine.pvoptimizer.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HomeHvacSettingsDto {

    @JsonProperty("set_temperature")
    private Double setTemperature;

    @JsonProperty("optimizer_enabled")
    private Boolean optimizerEnabled;

    public Double getSetTemperature() {
        return setTemperature;
    }

    public void setSetTemperature(Double setTemperature) {
        this.setTemperature = setTemperature;
    }

    public Boolean getOptimizerEnabled() {
        return optimizerEnabled;
    }

    public void setOptimizerEnabled(Boolean optimizerEnabled) {
        this.optimizerEnabled = optimizerEnabled;
    }
}
