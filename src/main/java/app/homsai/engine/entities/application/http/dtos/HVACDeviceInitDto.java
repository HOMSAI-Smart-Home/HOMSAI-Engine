package app.homsai.engine.entities.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HVACDeviceInitDto {

    @JsonProperty("hvac_devices_list")
    private List<HVACDeviceDto> hvacDeviceDtoList;

    @JsonProperty("initialization_time_secs")
    private Integer initTimeSecs;

    public List<HVACDeviceDto> getHvacDeviceDtoList() {
        return hvacDeviceDtoList;
    }

    public void setHvacDeviceDtoList(List<HVACDeviceDto> hvacDeviceDtoList) {
        this.hvacDeviceDtoList = hvacDeviceDtoList;
    }

    public Integer getInitTimeSecs() {
        return initTimeSecs;
    }

    public void setInitTimeSecs(Integer initTimeSecs) {
        this.initTimeSecs = initTimeSecs;
    }
}
