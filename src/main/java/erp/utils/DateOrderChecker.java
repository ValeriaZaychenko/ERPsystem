package erp.utils;


import erp.exceptions.DateOrderException;

import java.time.LocalDate;

public final class DateOrderChecker {

    public static void checkEndDateAfterBegin(LocalDate beginDate, LocalDate endDate) {
        if(beginDate.isAfter(endDate))
            throw new DateOrderException(beginDate, endDate);
    }
}
