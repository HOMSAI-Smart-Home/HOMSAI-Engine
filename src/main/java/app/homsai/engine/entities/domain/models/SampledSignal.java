package app.homsai.engine.entities.domain.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SampledSignal {

    private List<SampledSignalEntry> signal;

    public SampledSignal(){
        signal = new ArrayList<>();
    }

    public void addEntry(Double value){
        signal.add(new SampledSignalEntry(Instant.now(), value));
    }

    public List<SampledSignalEntry> getSignal() {
        return signal;
    }

    public Double getAverage(){
        if(signal.size() < 1)
            return 0D;
        double sum = signal.stream().mapToDouble(SampledSignalEntry::getValue).sum();
        return sum / signal.size();
    }
}
