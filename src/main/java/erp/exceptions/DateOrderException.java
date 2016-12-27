package erp.exceptions;

import erp.controller.constants.ErrorKeys;

import java.time.LocalDate;


public class DateOrderException extends DomainLogicException {

    public DateOrderException(LocalDate beginDate, LocalDate endDate) {
        super(beginDate + " " + endDate);
        setName(ErrorKeys.InvalidDateOrderMessage);
    }
}
