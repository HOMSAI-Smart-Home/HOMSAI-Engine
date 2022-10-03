package app.homsai.engine.forecast.gateways.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PVBalanceDto {

    @JsonProperty("bought_energy")
    private Double boughtEnergy;

    @JsonProperty("bought_energy_expense")
    private Double boughtEnergyExpense;

    @JsonProperty("sold_energy")
    private Double soldEnergy;

    @JsonProperty("sold_energy_earning")
    private Double soldEnergyEarning;

    @JsonProperty("self_consumption_percent")
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
