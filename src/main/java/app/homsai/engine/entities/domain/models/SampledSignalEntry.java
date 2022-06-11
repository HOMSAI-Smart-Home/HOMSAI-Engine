package app.homsai.engine.entities.domain.models;

import java.time.Instant;

public class SampledSignalEntry {

    private Instant date;

    private Double value;


    public SampledSignalEntry() {
    }

    public SampledSignalEntry(SampledSignalEntry sampledSignal) {
        this.date = sampledSignal.getDate();
        this.value = sampledSignal.getValue();
    }


    public SampledSignalEntry(Instant date, Double value) {
        this.date = date;
        this.value = value;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
