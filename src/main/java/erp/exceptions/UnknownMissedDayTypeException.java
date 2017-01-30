package erp.exceptions;


import erp.controller.constants.ErrorKeys;

public class UnknownMissedDayTypeException extends DomainLogicException {

    public UnknownMissedDayTypeException(String date) {
        super(date);
        setName(ErrorKeys.UnknownMissedDayTypeMessage);
    }
}
