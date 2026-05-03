package registry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        var metadata = new ModelMetadata(creator, LocalDate.now()); // Date-Time API
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

    // Streams & Sorting
    // A method to sort the registry by the model's name alphabetically
    public void printSortedModels() {
        System.out.println(ConsoleColors.YELLOW_BOLD + "--- Sorted Models (Alphabetical) ---" + ConsoleColors.RESET);
        registry.stream()
                .sorted(Comparator.comparing(AIModel::getName))
                .forEach(m -> System.out.println(ConsoleColors.CYAN + m.getName() + ConsoleColors.RESET));
    }

    // Stream Operations
    public void streamOperations() {
        System.out.println(ConsoleColors.YELLOW_BOLD + "\n--- Stream operations ---" + ConsoleColors.RESET);

        // 1. count()
        long totalModels = registry.stream().count();
        System.out.println("Total models in registry: " + totalModels);

        // 2. max() - Finding the model with the longest name
        Optional<AIModel> longestNameModel = registry.stream()
                .max(Comparator.comparingInt(m -> m.getName().length()));
        longestNameModel.ifPresent(m -> System.out.println("Model with longest name: " + m.getName()));

        // 3. anyMatch(), allMatch(), noneMatch()
        boolean hasTextModels = registry.stream().anyMatch(m -> m.getType() == ModelType.TEXT);
        boolean allAreText = registry.stream().allMatch(m -> m.getType() == ModelType.TEXT);
        boolean noAudioModels = registry.stream().noneMatch(m -> m.getType() == ModelType.AUDIO);

        System.out.println("Has TEXT models? " + hasTextModels);
        System.out.println("Are ALL models TEXT? " + allAreText);
        System.out.println("Are there ZERO AUDIO models? " + noAudioModels);

        // 4. map(), distinct(), limit(), and forEach()
        System.out.println("\nFirst 2 unique Model Types:");
        registry.stream()
                .map(AIModel::getType) // Extract just the type
                .distinct() // Remove duplicates
                .limit(2) // Only take the first 2
                .forEach(type -> System.out.println(" - " + type));

        // 5. filter() and findFirst()
        System.out.println("\nFinding first model that starts with 'P':");
        Optional<AIModel> pModel = registry.stream()
                .filter(m -> m.getName().startsWith("P"))
                .findFirst(); // Or findAny()
        pModel.ifPresent(m -> System.out.println("Found: " + m.getName()));

        // 6. collect() - groupingBy()
        System.out.println("\nGrouping models by Type:");
        Map<ModelType, List<AIModel>> modelsByType = registry.stream()
                .collect(Collectors.groupingBy(AIModel::getType));

        modelsByType.forEach((type, list) -> {
            System.out.println(type + " Models:");
            list.forEach(m -> System.out.println("  - " + m.getName()));
        });

        // 7. collect() - partitioningBy()
        System.out.println("\nPartitioning models by 'starts with C':");
        Map<Boolean, List<AIModel>> partitionedByC = registry.stream()
                .collect(Collectors.partitioningBy(m -> m.getName().startsWith("C")));

        System.out.println("Starts with C: " + partitionedByC.get(true).size() + " models");
        System.out.println("Doesn't start with C: " + partitionedByC.get(false).size() + " models");

        // 8. [Advanced: Java 25 Stream Gatherers]
        // Easily group items into "batches" or "windows" (e.g. batch size of 3)
        System.out.println("\nGathering models into batches of 3:");
        registry.stream()
                .gather(Gatherers.windowFixed(3))
                .forEach(batch -> {
                    System.out.print("Batch: ");
                    batch.forEach(m -> System.out.print(m.getName() + " | "));
                    System.out.println();
                });
    }

    // Concurrency with ExecutorService & Callables
    public void trainAllConcurrently() {
        System.out.println(ConsoleColors.YELLOW_BOLD + "\n--- Training Models Concurrently ---" + ConsoleColors.RESET);

        // Create a thread pool with 3 threads
        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {

            // Map our models to a list of Callable tasks
            List<Callable<String>> tasks = registry.stream().map(model -> (Callable<String>) () -> {
                model.train(); // Train the model
                return ConsoleColors.GREEN + model.getName() + " finished training on thread "
                        + Thread.currentThread().getName() + ConsoleColors.RESET;
            }).collect(Collectors.toList());

            // invokeAll processes all Callables in parallel
            List<Future<String>> results = executor.invokeAll(tasks);

            // Print the results as they finish
            for (Future<String> result : results) {
                System.out.println(result.get());
            }

        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Training interrupted: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    // MAIN METHOD: The Entry Point (Java 25 - Instance Main Method)
    void main() {
        RegistryManager manager = new RegistryManager();
        System.out.println(ConsoleColors.YELLOW_BOLD + "--- AI Model Registry ---" + ConsoleColors.RESET);

        try {
            // 1. Create a Record (Advanced)
            var meta = new ModelMetadata("OpenAI", LocalDate.now()); // Date-Time API

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

            // 8. Test Streams and Sorting
            manager.printSortedModels();

            // 9. Run Stream operations
            manager.streamOperations();

            // 10. Test Concurrency
            manager.trainAllConcurrently();

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