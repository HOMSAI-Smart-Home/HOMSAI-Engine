package app.homsai.engine.forecast.gateways.dtos;

/**
 * @author Giacomo Agostini on 06/10/2022
 */
public class PVForecastRequestDto {

    Integer days;

    Integer plantLifeDays;

    Double kWp;

    Double lat;

    Double lng;

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getPlantLifeDays() {
        return plantLifeDays;
    }

    public void setPlantLifeDays(Integer plantLifeDays) {
        this.plantLifeDays = plantLifeDays;
    }

    public Double getkWp() {
        return kWp;
    }

    public void setkWp(Double kWp) {
        this.kWp = kWp;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
