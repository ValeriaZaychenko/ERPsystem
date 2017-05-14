package erp.exceptions;

import erp.controller.constants.ErrorKeys;

public class DuplicateNameException extends DomainLogicException {

    public DuplicateNameException(String name) {
        super(name);
        setName(ErrorKeys.DuplicateNameException);
    }
}
