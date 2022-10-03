package app.homsai.engine.forecast.domain.models;

public class PVBalance {

    private Double boughtEnergy;

    private Double boughtEnergyExpense;

    private Double soldEnergy;

    private Double soldEnergyEarning;

    private Double selfConsumptionPercent;

    private Double balance;

    public Double getBoughtEnergy() {
        return boughtEnergy;
    }

    public void setBoughtEnergy(Double boughtEnergy) {
        this.boughtEnergy = boughtEnergy;
    }

    public Double getBoughtEnergyExpense() {
        return boughtEnergyExpense;
    }

    public void setBoughtEnergyExpense(Double boughtEnergyExpense) {
        this.boughtEnergyExpense = boughtEnergyExpense;
    }

    public Double getSoldEnergy() {
        return soldEnergy;
    }

    public void setSoldEnergy(Double soldEnergy) {
        this.soldEnergy = soldEnergy;
    }

    public Double getSoldEnergyEarning() {
        return soldEnergyEarning;
    }

    public void setSoldEnergyEarning(Double soldEnergyEarning) {
        this.soldEnergyEarning = soldEnergyEarning;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getSelfConsumptionPercent() {
        return selfConsumptionPercent;
    }

    public void setSelfConsumptionPercent(Double selfConsumptionPercent) {
        this.selfConsumptionPercent = selfConsumptionPercent;
    }
}
