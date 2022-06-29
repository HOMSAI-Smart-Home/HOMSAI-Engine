package app.homsai.engine.common.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class SettingsDto {

    @JsonProperty("general_power_meter_id")
    private String generalPowerMeterId;

    @JsonProperty("hvac_power_meter_id")
    private String hvacPowerMeterId;

    @JsonProperty("photovoltaic_production_meter_id")
    private String pvProductionSensorId;

    @JsonProperty("photovoltaic_storage_meter_id")
    private String pvStorageSensorId;

    @JsonProperty("photovoltaic_peak_power")
    private Double pvPeakPower;

    @JsonProperty("photovoltaic_install_date")
    private Instant pvInstallDate;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    public String getGeneralPowerMeterId() {
        return generalPowerMeterId;
    }

    public void setGeneralPowerMeterId(String generalPowerMeterId) {
        this.generalPowerMeterId = generalPowerMeterId;
    }

    public String getHvacPowerMeterId() {
        return hvacPowerMeterId;
    }

    public void setHvacPowerMeterId(String hvacPowerMeterId) {
        this.hvacPowerMeterId = hvacPowerMeterId;
    }

    public String getPvProductionSensorId() {
        return pvProductionSensorId;
    }

    public void setPvProductionSensorId(String pvProductionSensorId) {
        this.pvProductionSensorId = pvProductionSensorId;
    }

    public String getPvStorageSensorId() {
        return pvStorageSensorId;
    }

    public void setPvStorageSensorId(String pvStorageSensorId) {
        this.pvStorageSensorId = pvStorageSensorId;
    }

    public Double getPvPeakPower() {
        return pvPeakPower;
    }

    public void setPvPeakPower(Double pvPeakPower) {
        this.pvPeakPower = pvPeakPower;
    }

    public Instant getPvInstallDate() {
        return pvInstallDate;
    }

    public void setPvInstallDate(Instant pvInstallDate) {
        this.pvInstallDate = pvInstallDate;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
