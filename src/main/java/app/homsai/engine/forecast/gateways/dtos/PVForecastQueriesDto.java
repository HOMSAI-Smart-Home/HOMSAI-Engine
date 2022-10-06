package app.homsai.engine.forecast.gateways.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PVForecastQueriesDto {

    String date;

    @JsonProperty("minutes_in_day")
    Integer minutesInDay;

    Double production;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getMinutesInDay() {
        return minutesInDay;
    }

    public void setMinutesInDay(Integer minutesInDay) {
        this.minutesInDay = minutesInDay;
    }

    public Double getProduction() {
        return production;
    }

    public void setProduction(Double production) {
        this.production = production;
    }
}
