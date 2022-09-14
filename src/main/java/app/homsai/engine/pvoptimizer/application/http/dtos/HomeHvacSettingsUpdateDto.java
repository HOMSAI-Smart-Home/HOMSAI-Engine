package app.homsai.engine.pvoptimizer.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;

public class HomeHvacSettingsUpdateDto {

    @JsonProperty("set_temperature")
    private Double setTemperature;

    @JsonProperty("optimizer_enabled")
    private Boolean optimizerEnabled;

    @JsonProperty("optimizer_mode")
    private Integer optimizerMode;

    @JsonProperty("hvac_switch_entity_id")
    private String hvacSwitchEntityId;

    @JsonProperty("current_winter_hvac_equipment_uuid")
    private String currentWinterHVACEquipmentUuid;

    @JsonProperty("current_summer_hvac_equipment_uuid")
    private String currentSummerHVACEquipmentUuid;

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

    public String getHvacSwitchEntityId() {
        return hvacSwitchEntityId;
    }

    public void setHvacSwitchEntityId(String hvacSwitchEntityId) {
        this.hvacSwitchEntityId = hvacSwitchEntityId;
    }

    public String getCurrentWinterHVACEquipmentUuid() {
        return currentWinterHVACEquipmentUuid;
    }

    public void setCurrentWinterHVACEquipmentUuid(String currentWinterHVACEquipmentUuid) {
        this.currentWinterHVACEquipmentUuid = currentWinterHVACEquipmentUuid;
    }

    public String getCurrentSummerHVACEquipmentUuid() {
        return currentSummerHVACEquipmentUuid;
    }

    public void setCurrentSummerHVACEquipmentUuid(String currentSummerHVACEquipmentUuid) {
        this.currentSummerHVACEquipmentUuid = currentSummerHVACEquipmentUuid;
    }
}
