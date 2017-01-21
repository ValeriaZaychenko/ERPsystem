package erp.exceptions;

import erp.controller.constants.ErrorKeys;


public class ApplyGroupByException extends DomainLogicException {

    public ApplyGroupByException() {
        super("");
        setName(ErrorKeys.ApplyGroupByMessage);
    }
}
