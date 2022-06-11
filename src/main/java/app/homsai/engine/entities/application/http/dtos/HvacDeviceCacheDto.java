package app.homsai.engine.entities.application.http.dtos;

import app.homsai.engine.entities.domain.models.HVACDevice;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class HvacDeviceCacheDto {

    @JsonProperty("in_progress")
    private Boolean inProgress;

    @JsonProperty("elapsed_time_seconds")
    private Integer elapsedTimeSeconds;

    @JsonProperty("remaining_time_seconds")
    private Integer remainingTimeSeconds;

    @JsonProperty("total_time_seconds")
    private Integer totalTimeSeconds;

    @JsonProperty("log")
    private String log;

    @JsonProperty("synchronized_hvac_devices")
    List<HVACDeviceDto> syncedHvacDevices;

    public HvacDeviceCacheDto() {
        inProgress = false;
        log = "";
        elapsedTimeSeconds = 0;
        totalTimeSeconds=0;
        remainingTimeSeconds=0;
    }

    public Boolean getInProgress() {
        return inProgress;
    }

    public void setInProgress(Boolean inProgress) {
        this.inProgress = inProgress;
    }

    public Integer getElapsedTimeSeconds() {
        return elapsedTimeSeconds;
    }

    public void setElapsedTimeSeconds(Integer elapseTimeSeconds) {
        this.elapsedTimeSeconds = elapseTimeSeconds;
    }

    public Integer getRemainingTimeSeconds() {
        return remainingTimeSeconds;
    }

    public void setRemainingTimeSeconds(Integer remainingTimeSeconds) {
        this.remainingTimeSeconds = remainingTimeSeconds;
    }

    @JsonProperty("elapsed_percent")
    public Double getElapsedPercent() {
        if(totalTimeSeconds==0)
            return 0D;
        return elapsedTimeSeconds.doubleValue() / totalTimeSeconds.doubleValue() * 100D;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Integer getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public void setTotalTimeSeconds(Integer totalTimeSeconds) {
        this.totalTimeSeconds = totalTimeSeconds;
    }

    public List<HVACDeviceDto> getSyncedHvacDevices() {
        return syncedHvacDevices;
    }

    public void setSyncedHvacDevices(List<HVACDeviceDto> syncedHvacDevices) {
        this.syncedHvacDevices = syncedHvacDevices;
    }

    public void addLog(String log) {
        this.log += (log + "<br>");
    }

    public void addTime(Integer time){
        this.elapsedTimeSeconds += time;
        this.remainingTimeSeconds -= time;
    }

    public void addDevice(HVACDeviceDto syncedDevice) {
        if(syncedHvacDevices == null)
            syncedHvacDevices = new ArrayList<>();
        syncedHvacDevices.add(syncedDevice);
    }
}
