package app.homsai.engine.entities.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

public class HomsaiEntitiesHistoricalStateLightDto {

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("value")
    private Double value;

    @JsonProperty("unit_of_measurement")
    private String unitOfMeasurement;


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

}

