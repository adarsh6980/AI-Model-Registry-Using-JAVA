// package registry;

// // This class tries to extend AIModel...
// // ...but since 'UnauthorizedModel' is NOT in the 'permits' list of AIModel,
// it will throw an error. Beacuse i am using sealed class in LLM
// public final class UnauthorizedModel extends AIModel {

// public UnauthorizedModel(String name) {
// super(name, ModelType.IMAGE);
// }

// @Override
// public void train() {
// System.out.println("Trying to break in...");
// }
// }