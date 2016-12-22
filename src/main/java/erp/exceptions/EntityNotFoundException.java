package erp.exceptions;


public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String className) {
        super(className);
    }
}
