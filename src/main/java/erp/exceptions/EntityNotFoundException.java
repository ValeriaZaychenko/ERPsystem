package erp.exceptions;


public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String className) {
        super("Database doesn't have entity with name" + className + ".");
    }
}
