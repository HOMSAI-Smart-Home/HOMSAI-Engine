package app.homsai.engine.pvoptimizer.domain.models;

public class InitConsumptionObj {

    private HVACDevice hvacDevice;

    private Double singleDeviceConsumption;

    private Double coupledDeviceConsumption;

    private Double nextOldSetTemp;

    public HVACDevice getHvacDevice() {
        return hvacDevice;
    }

    public void setHvacDevice(HVACDevice hvacDevice) {
        this.hvacDevice = hvacDevice;
    }

    public Double getSingleDeviceConsumption() {
        return singleDeviceConsumption;
    }

    public void setSingleDeviceConsumption(Double singleDeviceConsumption) {
        this.singleDeviceConsumption = singleDeviceConsumption;
    }

    public Double getCoupledDeviceConsumption() {
        return coupledDeviceConsumption;
    }

    public void setCoupledDeviceConsumption(Double coupledDeviceConsumption) {
        this.coupledDeviceConsumption = coupledDeviceConsumption;
    }

    public Double getNextOldSetTemp() {
        return nextOldSetTemp;
    }

    public void setNextOldSetTemp(Double nextOldSetTemp) {
        this.nextOldSetTemp = nextOldSetTemp;
    }
}
