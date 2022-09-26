package app.homsai.engine.entities.domain.models;

import app.homsai.engine.common.domain.models.BaseEntity;
import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.pvoptimizer.domain.models.HVACEquipment;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @Column(name = "hvac_summer_power_meter_id")
    private String hvacSummerPowerMeterId;

    @Column(name = "hvac_winter_power_meter_id")
    private String hvacWinterPowerMeterId;

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

    @Column(name = "photovoltaic_optimizations_enabled")
    private Boolean pvOptimizationsEnabled;

    @Column(name = "aiservice_email")
    private String aiserviceEmail;

    @Column(name = "aiservice_token")
    private String aiserviceToken;

    @Column(name = "aiservice_refresh_token")
    private String aiserviceRefreshToken;

    @Column(name= "optimizer_Mode")
    private Integer optimizerMode;

    @Column(name = "hvac_switch_entity_id")
    private String hvacSwitchEntityId;

    @ManyToOne
    @JoinColumn(name = "current_winter_hvac_equipment_uuid")
    private HVACEquipment currentWinterHVACEquipment;

    @ManyToOne
    @JoinColumn(name = "current_summer_hvac_equipment_uuid")
    private HVACEquipment currentSummerHVACEquipment;


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

    public String getHvacPowerMeterId(Integer type) {
        if(type == Consts.HVAC_MODE_WINTER_ID && hvacWinterPowerMeterId != null)
            return hvacWinterPowerMeterId;
        if(type == Consts.HVAC_MODE_SUMMER_ID && hvacSummerPowerMeterId != null)
            return hvacSummerPowerMeterId;
        return hvacPowerMeterId;
    }

    public String getHvacPowerMeterId() {
        return hvacPowerMeterId;
    }

    public void setHvacPowerMeterId(String hvacPowerMeterId) {
        this.hvacPowerMeterId = hvacPowerMeterId;
    }

    public String getHvacSummerPowerMeterId() {
        if(hvacSummerPowerMeterId == null)
            return hvacPowerMeterId;
        return hvacSummerPowerMeterId;
    }

    public void setHvacSummerPowerMeterId(String hvacSummerPowerMeterId) {
        this.hvacSummerPowerMeterId = hvacSummerPowerMeterId;
    }

    public String getHvacWinterPowerMeterId() {
        if(hvacWinterPowerMeterId == null)
            return hvacPowerMeterId;
        return hvacWinterPowerMeterId;
    }

    public void setHvacWinterPowerMeterId(String hvacWinterPowerMeterId) {
        this.hvacWinterPowerMeterId = hvacWinterPowerMeterId;
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

    public Boolean getPvOptimizationsEnabled() {
        return pvOptimizationsEnabled;
    }

    public void setPvOptimizationsEnabled(Boolean pvOptimizationsEnabled) {
        this.pvOptimizationsEnabled = pvOptimizationsEnabled;
    }

    public String getAiserviceToken() {
        return aiserviceToken;
    }

    public void setAiserviceToken(String aiserviceToken) {
        this.aiserviceToken = aiserviceToken;
    }

    public String getAiserviceRefreshToken() {
        return aiserviceRefreshToken;
    }

    public void setAiserviceRefreshToken(String aiserviceRefreshToken) {
        this.aiserviceRefreshToken = aiserviceRefreshToken;
    }

    public String getAiserviceEmail() {
        return aiserviceEmail;
    }

    public void setAiserviceEmail(String aiserviceEmail) {
        this.aiserviceEmail = aiserviceEmail;
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

    public HVACEquipment getCurrentWinterHVACEquipment() {
        return currentWinterHVACEquipment;
    }

    public void setCurrentWinterHVACEquipment(HVACEquipment currentWinterHVACEquipment) {
        this.currentWinterHVACEquipment = currentWinterHVACEquipment;
    }

    public HVACEquipment getCurrentSummerHVACEquipment() {
        return currentSummerHVACEquipment;
    }

    public void setCurrentSummerHVACEquipment(HVACEquipment currentSummerHVACEquipment) {
        this.currentSummerHVACEquipment = currentSummerHVACEquipment;
    }
}

