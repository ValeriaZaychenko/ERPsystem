package erp.exceptions;


public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super(email);
    }
}
