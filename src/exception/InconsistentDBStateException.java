package exception;

public class InconsistentDBStateException extends RuntimeException {

    public InconsistentDBStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
