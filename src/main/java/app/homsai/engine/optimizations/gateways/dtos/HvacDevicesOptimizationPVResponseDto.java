package app.homsai.engine.optimizations.gateways.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HvacDevicesOptimizationPVResponseDto {

    @JsonProperty("devices_to_turn_on")
    List<String> devicesToTurnOn;

    @JsonProperty("devices_to_turn_off")
    List<String> devicesToTurnOff;

    public List<String> getDevicesToTurnOn() {
        return devicesToTurnOn;
    }

    public void setDevicesToTurnOn(List<String> devicesToTurnOn) {
        this.devicesToTurnOn = devicesToTurnOn;
    }

    public List<String> getDevicesToTurnOff() {
        return devicesToTurnOff;
    }

    public void setDevicesToTurnOff(List<String> devicesToTurnOff) {
        this.devicesToTurnOff = devicesToTurnOff;
    }
}
