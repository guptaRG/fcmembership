package exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class entityClass, Throwable cause) {
        super(String.format("%s not found", entityClass), cause);
    }
}
