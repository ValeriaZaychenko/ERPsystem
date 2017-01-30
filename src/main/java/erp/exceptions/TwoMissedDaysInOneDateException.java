package erp.exceptions;


import erp.controller.constants.ErrorKeys;

public class TwoMissedDaysInOneDateException extends DomainLogicException {

    public TwoMissedDaysInOneDateException(String date) {
        super(date);
        setName(ErrorKeys.TwoMissedDaysInOneDateMessage);
    }
}
