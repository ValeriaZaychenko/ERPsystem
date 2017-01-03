package erp.service.impl;


import erp.service.IDayCounterService;
import erp.utils.DateOrderChecker;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class DayCounterService implements IDayCounterService{

    @Override
    public int countWeekendsBetweenDates(LocalDate begin, LocalDate end) {
        DateOrderChecker.checkEndDateAfterBegin(begin, end);

        int result = 0;
        while (begin.isBefore(end) || begin.isEqual(end)) {
            if (begin.getDayOfWeek() == DayOfWeek.SATURDAY || begin.getDayOfWeek() == DayOfWeek.SUNDAY)
                result++;
            begin = begin.plusDays(1);
        }

        return result;
    }

    @Override
    public int getAllDaysQuantityBetweenDates(LocalDate begin, LocalDate end) {
        DateOrderChecker.checkEndDateAfterBegin(begin, end);

        return (int) ChronoUnit.DAYS.between(begin, end) + 1;
    }
}
