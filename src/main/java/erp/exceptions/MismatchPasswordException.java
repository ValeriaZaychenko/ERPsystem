package erp.exceptions;


public class MismatchPasswordException extends RuntimeException {

    public MismatchPasswordException() {
        super("Password doesn't match with old password");
    }
}
