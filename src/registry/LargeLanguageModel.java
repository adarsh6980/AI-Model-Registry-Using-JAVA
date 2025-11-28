package registry;

// Inheritance
// extends AIModel means: "I am taking the Abstract class as a parent"
public final class LargeLanguageModel extends AIModel {

    private int parameterCount; // Represents the number of parameters in the large language model.
    private ModelMetadata metadata; // Stores additional, descriptive metadata about the model.

    // Constructor matching the Parent
    public LargeLanguageModel(String name, int parameterCount, ModelMetadata metadata) {
        // super() calls the parent's constructor to set the name and type
        super(name, ModelType.TEXT);
        this.parameterCount = parameterCount;
        this.metadata = metadata;
    }

    // Overriding
    // HERE is where i use 'train' from the parent class AIModel! We fill in the
    // blank.
    @Override
    public void train() {
        System.out.println(ConsoleColors.PURPLE + "Training " + getName() + " on text data..." + ConsoleColors.RESET);
    }

    // HERE is where i use 'runInference' from the class Computable!
    @Override
    public void runInference() {
        System.out.println(ConsoleColors.CYAN + "Generating text response..." + ConsoleColors.RESET);
    }

    // Switch Expression
    public String getTrainingDuration() {
        return switch (this.type) {
            case TEXT -> "2 Weeks";
            case IMAGE -> "4 Weeks";
            case AUDIO -> "1 Week";
        };
    }

    public void printDetails() {
        System.out.println(ConsoleColors.CYAN + "Model: " + getName() + " | Duration: " + getTrainingDuration()
                + ConsoleColors.RESET);
    }
}