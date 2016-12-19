package erp.exceptions;


public class UnknownRoleException extends RuntimeException {

    public UnknownRoleException(String role) {
        super("Can't parse role from " + role + ". Must be ADMIN, LEADER or USER.");
    }
}
