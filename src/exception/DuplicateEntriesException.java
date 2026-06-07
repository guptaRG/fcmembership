package exception;

public class DuplicateEntriesException extends RuntimeException {
    public DuplicateEntriesException(Class entityClass, Throwable cause) {
        super(String.format("%s cannot save duplicate entries", entityClass), cause);
    }

}
