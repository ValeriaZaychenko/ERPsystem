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
import java.util.HashMap;
import java.util.Map;

@Service
public class CalendarService implements ICalendarService {

    @Inject
    private HolidayRepository holidayRepository;

    private Map<String, Integer> calendar = new HashMap<>();

    @Override
    public CalendarDto getCalendarInformationBetweenDates(LocalDate begin, LocalDate end) {
        DateOrderChecker.checkEndDateAfterBegin(begin, end);

        //DON'T TOUCH THE ORDER
        countWeekendsBetweenDates(begin, end);
        countHolidaysBetweenDates(begin, end);
        countAllDaysQuantityBetweenDates(begin, end);
        countWorkdaysQuantityBetweenDates();


        return DtoBuilder.calendarToDto(
                calendar.get("weekends"),
                calendar.get("holidays"),
                calendar.get("workdays"),
                calendar.get("all")
        );
    }

    private void countWeekendsBetweenDates(LocalDate begin, LocalDate end) {
        int result = 0;
        while (begin.isBefore(end) || begin.isEqual(end)) {
            if (begin.getDayOfWeek() == DayOfWeek.SATURDAY || begin.getDayOfWeek() == DayOfWeek.SUNDAY)
                result++;
            begin = begin.plusDays(1);
        }

        calendar.put("weekends", result);
    }

    private void countHolidaysBetweenDates(LocalDate begin, LocalDate end) {
        calendar.put("holidays", holidayRepository.findByDateBetweenQueryOrderByDateDesc(begin, end).size());
    }

    private void countAllDaysQuantityBetweenDates(LocalDate begin, LocalDate end) {
        calendar.put("all", (int) ChronoUnit.DAYS.between(begin, end) + 1);
    }

    private void countWorkdaysQuantityBetweenDates() {
        calendar.put("workdays", calendar.get("all") - calendar.get("weekends") - calendar.get("holidays"));
    }
}
