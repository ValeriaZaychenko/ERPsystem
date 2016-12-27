package erp.exceptions;


import erp.controller.constants.ErrorKeys;

public class InvalidDateException extends DomainLogicException {

    public InvalidDateException(String date) {
        super(date);
        setName(ErrorKeys.InvalidDateMessage);
    }
}
