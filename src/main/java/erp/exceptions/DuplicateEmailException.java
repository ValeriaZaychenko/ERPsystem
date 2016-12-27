package erp.exceptions;


import erp.controller.constants.ErrorKeys;

public class DuplicateEmailException extends DomainLogicException {

    public DuplicateEmailException(String email) {
        super(email);
        setName(ErrorKeys.DuplicateEmailMessage);
    }
}
