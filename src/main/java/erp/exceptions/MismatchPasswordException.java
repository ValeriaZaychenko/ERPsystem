package erp.exceptions;


import erp.controller.constants.ErrorKeys;

public class MismatchPasswordException extends DomainLogicException {

    public MismatchPasswordException() {
        super("");
        setName(ErrorKeys.MismatchPasswordMessage);
    }
}
