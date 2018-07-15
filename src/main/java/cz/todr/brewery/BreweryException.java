package cz.todr.brewery;

public class BreweryException extends RuntimeException {

    public BreweryException() {
    }

    public BreweryException(String message) {
        super(message);
    }

    public BreweryException(String message, Throwable cause) {
        super(message, cause);
    }

    public BreweryException(Throwable cause) {
        super(cause);
    }
}
