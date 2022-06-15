package app.homsai.engine.optimizations.gateways.dtos;

import app.homsai.engine.optimizations.application.http.dtos.HvacDeviceDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HvacOptimizationPVRequestDto {

    @JsonProperty("general_power_meter_value")
    private Double generalPowerMeterValue;

    @JsonProperty("photovoltaic_power_meter_value")
    private Double photovoltaicPowerMeterValue;

    @JsonProperty("storage_power_meter_value")
    private Double storagePowerMeterValue;

    @JsonProperty("hvac_devices")
    private List<HvacDeviceDto> hvacDevices;

    @JsonProperty("minimum_execution_time_minutes")
    private Integer minimumExecutionTimeMinutes;

    @JsonProperty("minimum_idle_time_minutes")
    private Integer minimumIdleTimeMinutes;

    @JsonProperty("cycle_time")
    private Integer cycleTime;

    public Double getGeneralPowerMeterValue() {
        return generalPowerMeterValue;
    }

    public void setGeneralPowerMeterValue(Double generalPowerMeterValue) {
        this.generalPowerMeterValue = generalPowerMeterValue;
    }

    public Double getPhotovoltaicPowerMeterValue() {
        return photovoltaicPowerMeterValue;
    }

    public void setPhotovoltaicPowerMeterValue(Double photovoltaicPowerMeterValue) {
        this.photovoltaicPowerMeterValue = photovoltaicPowerMeterValue;
    }

    public Double getStoragePowerMeterValue() {
        return storagePowerMeterValue;
    }

    public void setStoragePowerMeterValue(Double storagePowerMeterValue) {
        this.storagePowerMeterValue = storagePowerMeterValue;
    }

    public List<HvacDeviceDto> getHvacDevices() {
        return hvacDevices;
    }

    public void setHvacDevices(List<HvacDeviceDto> hvacDevices) {
        this.hvacDevices = hvacDevices;
    }

    public Integer getMinimumExecutionTimeMinutes() {
        return minimumExecutionTimeMinutes;
    }

    public void setMinimumExecutionTimeMinutes(Integer minimumExecutionTimeMinutes) {
        this.minimumExecutionTimeMinutes = minimumExecutionTimeMinutes;
    }

    public Integer getMinimumIdleTimeMinutes() {
        return minimumIdleTimeMinutes;
    }

    public void setMinimumIdleTimeMinutes(Integer minimumIdleTimeMinutes) {
        this.minimumIdleTimeMinutes = minimumIdleTimeMinutes;
    }

    public Integer getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(Integer cycleTime) {
        this.cycleTime = cycleTime;
    }
}
