package erp.service.impl;

import erp.dto.CalendarDto;
import erp.repository.HolidayRepository;
import erp.service.ICalendarService;
import erp.utils.DateOrderChecker;
import erp.utils.DtoBuilder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CalendarService implements ICalendarService {

    @Inject
    private HolidayRepository holidayRepository;

    @Override
    public CalendarDto getCalendarInformationBetweenDates(LocalDate begin, LocalDate end) {
        DateOrderChecker.checkEndDateAfterBegin(begin, end);

        return DtoBuilder.calendarToDto(
                countWeekendsBetweenDates(begin, end),
                countHolidaysBetweenDates(begin, end),
                countWorkdaysQuantityBetweenDates(begin, end),
                countAllDaysQuantityBetweenDates(begin, end)
        );
    }

    private int countWeekendsBetweenDates(LocalDate begin, LocalDate end) {
        int result = 0;
        while (begin.isBefore(end) || begin.isEqual(end)) {
            if (begin.getDayOfWeek() == DayOfWeek.SATURDAY || begin.getDayOfWeek() == DayOfWeek.SUNDAY)
                result++;
            begin = begin.plusDays(1);
        }

        return result;
    }

    private int countHolidaysBetweenDates(LocalDate begin, LocalDate end) {
        return holidayRepository.findByDateBetweenQueryOrderByDateDesc(begin, end).size();
    }

    private int countAllDaysQuantityBetweenDates(LocalDate begin, LocalDate end) {
        return (int) ChronoUnit.DAYS.between(begin, end) + 1;
    }

    private int countWorkdaysQuantityBetweenDates(LocalDate begin, LocalDate end) {
        return countAllDaysQuantityBetweenDates(begin, end)
                - countWeekendsBetweenDates(begin, end)
                - countHolidaysBetweenDates(begin, end);
    }
}
