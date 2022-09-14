package app.homsai.engine.pvoptimizer.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HomeHvacSettingsDto {

    @JsonProperty("set_temperature")
    private Double setTemperature;

    @JsonProperty("optimizer_enabled")
    private Boolean optimizerEnabled;

    @JsonProperty("optimizer_mode")
    private Integer optimizerMode;

    @JsonProperty("current_winter_hvac_equipment")
    private HVACEquipmentDto currentWinterHVACEquipment;

    @JsonProperty("current_summer_hvac_equipment")
    private HVACEquipmentDto currentSummerHVACEquipment;

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

    public Integer getOptimizerMode() {
        return optimizerMode;
    }

    public void setOptimizerMode(Integer optimizerMode) {
        this.optimizerMode = optimizerMode;
    }

    public HVACEquipmentDto getCurrentWinterHVACEquipment() {
        return currentWinterHVACEquipment;
    }

    public void setCurrentWinterHVACEquipment(HVACEquipmentDto currentWinterHVACEquipment) {
        this.currentWinterHVACEquipment = currentWinterHVACEquipment;
    }

    public HVACEquipmentDto getCurrentSummerHVACEquipment() {
        return currentSummerHVACEquipment;
    }

    public void setCurrentSummerHVACEquipment(HVACEquipmentDto currentSummerHVACEquipment) {
        this.currentSummerHVACEquipment = currentSummerHVACEquipment;
    }
}
