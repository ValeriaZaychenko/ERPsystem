package erp.exceptions;


public class UnknownRoleException extends RuntimeException {

    public UnknownRoleException(String role) {
        super(role);
    }
}
