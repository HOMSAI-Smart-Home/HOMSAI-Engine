package app.homsai.engine.pvoptimizer.domain.models;

public class DoubleCircularArray {

    private static final int DEFAULT_CAPACITY = 10;
    private final int capacity;
    private double[] data;
    private int writeSequence;

    public DoubleCircularArray(int capacity, double defaultValue) {
        this.capacity = (capacity < 1) ? DEFAULT_CAPACITY : capacity;
        this.data = new double[this.capacity];
        this.writeSequence = -1;
        for(int i = 0; i<capacity; i++){
            this.data[i] = defaultValue;
        }
    }

    public boolean insert(double element) {
        writeSequence = (writeSequence + 1) % capacity;
        data[writeSequence] = element;
        return true;
    }

    public Double getAverageValue(){
        double sum = 0d;
        for(double d : this.data)
            sum+=d;
        return sum / this.capacity;
    }
}
