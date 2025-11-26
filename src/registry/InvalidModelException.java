package registry;

// Exceptions ]
// This class allows us to throw a specific error when something goes wrong.
public class InvalidModelException extends Exception {
    public InvalidModelException(String message) {
        super(message); // Passes the error message up to the standard Java Exception system
    }
}