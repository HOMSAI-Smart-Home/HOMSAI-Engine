package app.homsai.engine.entities.domain.models;

import app.homsai.engine.common.domain.models.BaseEntity;

import javax.persistence.*;
import java.time.Instant;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Entity
@Table(name = "home_info")
public class HomeInfo extends BaseEntity {

    @Column(name = "has_photovoltaic")
    private Boolean hasPV;

    @Column(name = "general_power_meter_id")
    private String generalPowerMeterId;

    @Column(name = "hvac_power_meter_id")
    private String hvacPowerMeterId;

    @Column(name = "photovoltaic_production_sensor_id")
    private String pvProductionSensorId;

    @Column(name = "photovoltaic_storage_sensor_id")
    private String pvStorageSensorId;

    @Column(name = "photovoltaic_peak_power")
    private Double pvPeakPower;

    @Column(name = "photovoltaic_install_date")
    private Instant pvInstallDate;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    public Boolean getHasPV() {
        return hasPV;
    }

    public void setHasPV(Boolean hasPV) {
        this.hasPV = hasPV;
    }

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




}

