package erp.service;


import erp.dto.CalendarDto;

import java.time.LocalDate;

public interface ICalendarService {

    CalendarDto getCalendarInformationBetweenDates(LocalDate begin, LocalDate end);
}
