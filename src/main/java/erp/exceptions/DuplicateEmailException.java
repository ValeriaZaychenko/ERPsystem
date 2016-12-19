package erp.exceptions;


public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Database already has user with email: " + email);
    }
}
