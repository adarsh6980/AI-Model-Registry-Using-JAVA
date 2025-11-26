package registry;

import java.util.ArrayList;
import java.util.List;

// Abstract Class
public abstract class AIModel implements Computable {

    // Encapsulation
    private String name;

    // I used 'protected' so the Child class can see this and use it for the Switch case
    protected ModelType type;

    // Java Core API
    protected List<Double> versionHistory;

    // Constructor
    public AIModel(String name, ModelType type) {
        this.name = name;
        this.type = type;
        this.versionHistory = new ArrayList<>();
        this.versionHistory.add(1.0);
    }

    public String getName() {
        return name;
    }

    public ModelType getType() {
        return type;
    }

    // Abstract Method
    public abstract void train();
}