package erp.exceptions;


import erp.controller.constants.ErrorKeys;

public class UnknownRoleException extends DomainLogicException {

    public UnknownRoleException(String role) {
        super(role);
        setName(ErrorKeys.UnknownRoleMessage);
    }
}
