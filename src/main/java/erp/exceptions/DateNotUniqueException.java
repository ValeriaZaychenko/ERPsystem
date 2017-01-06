package erp.exceptions;


import erp.controller.constants.ErrorKeys;

import java.time.LocalDate;

public class DateNotUniqueException extends DomainLogicException {

    public DateNotUniqueException(LocalDate date) {
        super(date.toString());
        setName(ErrorKeys.DateNotUniqueMessage);
    }
}
