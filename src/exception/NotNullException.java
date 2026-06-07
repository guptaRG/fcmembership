package exception;

public class NotNullException extends RuntimeException {

    public NotNullException(String field, Throwable cause) {
        super(String.format("%s cannot be null", field));
    }

}
