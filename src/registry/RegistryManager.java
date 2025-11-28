package registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate; // <--- [Advanced- lambda]

public class RegistryManager {

    // Java Core API
    // Using a List to store our models.
    private List<AIModel> registry = new ArrayList<>();

    // Method Overloading 1
    // Method 1: Takes a pre-made model object
    public void register(AIModel model) {
        registry.add(model);
        System.out.println(ConsoleColors.GREEN + "Registered: " + model.getName() + ConsoleColors.RESET);
    }

    // Method Overloading 2
    // Method 2: Takes raw data and builds the model for you
    public void register(String name, int params, String creator) throws InvalidModelException {
        if (params < 0) {
            throw new InvalidModelException("Parameters cannot be negative!");
        }

        // LVTI: Using 'var' to let Java guess the type
        var metadata = new ModelMetadata(creator, "2024-11-28");
        var model = new LargeLanguageModel(name, params, metadata);
        registry.add(model);
        System.out
                .println(ConsoleColors.GREEN + "Registered via overloading: " + model.getName() + ConsoleColors.RESET);
    }

    // Varargs
    public void displayMany(AIModel... models) { // The '...' means we can pass 1 model, 2 models, or 100 models
                                                 // separated by commas
        for (AIModel m : models) {
            System.out.println(ConsoleColors.BLUE + "Displaying: " + m.getName() + ConsoleColors.RESET);
        }
    }

    // [Advanced: Lambdas & Predicate]
    // This method takes a 'rule' (condition) as a parameter.
    // It checks every model against that rule.
    public void filterAndPrint(Predicate<AIModel> condition) {
        System.out.println(ConsoleColors.YELLOW_BOLD + "--- Filtering Models ---" + ConsoleColors.RESET);
        for (AIModel m : registry) {
            // condition.test(m) runs the lambda rule on this model
            if (condition.test(m)) {
                System.out.println(ConsoleColors.GREEN + "Match found: " + m.getName() + ConsoleColors.RESET);
            }
        }
    }

    // MAIN METHOD: The Entry Point
    public static void main(String[] args) {
        RegistryManager manager = new RegistryManager();
        System.out.println(ConsoleColors.YELLOW_BOLD + "--- AI Model Registry ---" + ConsoleColors.RESET);

        try {
            // 1. Create a Record (Advanced)
            var meta = new ModelMetadata("OpenAI", "2023-01-01");

            // 2. Create a Class (Fundamental)
            LargeLanguageModel gpt4 = new LargeLanguageModel("GPT-4", 1000, meta);
            LargeLanguageModel gemini = new LargeLanguageModel("Gemini-2.0", 700, meta);
            LargeLanguageModel perplexity = new LargeLanguageModel("Perplexity-2.0", 700, meta);
            LargeLanguageModel claude = new LargeLanguageModel("Claude-3", 700, meta);
            // 3. Register it using Method 1
            manager.register(gpt4);
            manager.register(gemini);
            manager.register(perplexity);
            manager.register(claude);
            // 4. Register another using Method 2 (Overloading)
            manager.register("Llama-3", 700, "Meta");
            manager.register("Claude-3", 700, "Anthropic");
            manager.register("Perplexity-3.0", 700, "Anthropic");
            manager.register("Cork-2.0", 700, "Anthropic");

            // 4. Display many models
            manager.displayMany(gpt4, gemini);

            // 5. Test the Advanced Switch Expression
            gpt4.printDetails();
            claude.printDetails();
            perplexity.printDetails();
            gemini.printDetails();

            // 7. Test the Advanced Lambda Expression
            manager.filterAndPrint(m -> m.getName().startsWith("G"));

            // 6. Force an error to test Exception Handling
            // 6. Force an error to test Exception Handling
            try {
                manager.register("BadModel", -5, "Unknown");
            } catch (InvalidModelException e) {
                System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
            }

            throw new RuntimeException("Testing Java 22");

        } catch (Exception _) {
            // Unnamed Variable
            // Java 22 Unnamed Variable
            // We use '_' because we don't care about the variable name here.
            System.out.println(ConsoleColors.RED + "Java 22 : " + "An unknown error occurred." + ConsoleColors.RESET);
        }
    }
}