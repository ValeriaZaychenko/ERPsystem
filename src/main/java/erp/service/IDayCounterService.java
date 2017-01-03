package erp.service;

import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public interface IDayCounterService {

    int countWeekendsBetweenDates(
            LocalDate begin,
            LocalDate end);

    int getAllDaysQuantityBetweenDates(
            LocalDate begin,
            LocalDate end);
}
