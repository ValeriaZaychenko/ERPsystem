package erp.service.impl;

import erp.dto.CalendarDto;
import erp.exceptions.DateOrderException;
import erp.service.ICalendarService;
import erp.service.IHolidayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional
@WithMockUser(username = "ram", authorities={"AUTH_ADMIN"})
public class CalendarServiceTest {

    @Inject
    private ICalendarService calendarService;
    @Inject
    private IHolidayService holidayService;

    //---COUNT WEEKENDS TESTS-------------------------------------------------------------------------------------------

    @Test(expected = DateOrderException.class)
    public void countWeekendsEndBeforeBeginDate() {
        LocalDate begin = LocalDate.of(2016, 12, 31);
        LocalDate end = LocalDate.of(2016, 12, 1);
        calendarService.getCalendarInformationBetweenDates(begin, end);
    }

    @Test
    public void countWeekendsCorrectly() {
        LocalDate begin = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2016, 12, 31);
        int days = calendarService.getCalendarInformationBetweenDates(begin, end).getWeekends();

        assertEquals(days, 9);
    }

    @Test
    public void countWeekendsCorrectlyBeginFromSaturday() {
        LocalDate begin = LocalDate.of(2016, 12, 3);
        LocalDate end = LocalDate.of(2016, 12, 31);
        int days = calendarService.getCalendarInformationBetweenDates(begin, end).getWeekends();

        assertEquals(days, 9);
    }

    @Test
    public void countWeekendsCorrectlyBeginFromSunday() {
        LocalDate begin = LocalDate.of(2016, 12, 4);
        LocalDate end = LocalDate.of(2016, 12, 31);
        int days = calendarService.getCalendarInformationBetweenDates(begin, end).getWeekends();

        assertEquals(days, 8);
    }

    @Test
    public void countWeekendsCorrectlyEndsBySaturday() {
        LocalDate begin = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2016, 12, 10);
        int days = calendarService.getCalendarInformationBetweenDates(begin, end).getWeekends();

        assertEquals(days, 3);
    }

    @Test
    public void countWeekendsCorrectlyEndsBySunday() {
        LocalDate begin = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2016, 12, 11);
        int days = calendarService.getCalendarInformationBetweenDates(begin, end).getWeekends();

        assertEquals(days, 4);
    }

    @Test
    public void countWeekendsCorrectlyBeginsFromSaturdayEndsBySunday() {
        LocalDate begin = LocalDate.of(2016, 12, 3);
        LocalDate end = LocalDate.of(2016, 12, 11);
        int days = calendarService.getCalendarInformationBetweenDates(begin, end).getWeekends();

        assertEquals(days, 4);
    }

    //---COUNT HOLIDAYS TESTS-------------------------------------------------------------------------------------------

    @Test(expected = DateOrderException.class)
    public void countHolidaysBeginAfterEndDate() {
        calendarService.getCalendarInformationBetweenDates(LocalDate.now(), LocalDate.now().minusDays(1)).getHolidays();
    }

    @Test
    public void countHolidaysCorrectly() {
        holidayService.createHoliday(LocalDate.now(), "sss");
        int days = calendarService.getCalendarInformationBetweenDates(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1))
                .getHolidays();

        assertEquals(days, 1);
    }

    @Test
    public void countHolidayTodayIncludeBeginDateCorrectly() {
        holidayService.createHoliday(LocalDate.now(), "sss");
        int days = calendarService.getCalendarInformationBetweenDates(
                LocalDate.now(),
                LocalDate.now().plusDays(1))
                .getHolidays();

        assertEquals(days, 1);
    }

    @Test
    public void countHolidayTodayIncludeEndDateCorrectly() {
        holidayService.createHoliday(LocalDate.now(), "sss");
        int days = calendarService.getCalendarInformationBetweenDates(
                LocalDate.now().minusDays(1),
                LocalDate.now())
                .getHolidays();

        assertEquals(days, 1);
    }

    @Test
    public void countHolidayTodayDateCorrectly() {
        holidayService.createHoliday(LocalDate.now(), "sss");
        holidayService.createHoliday(LocalDate.of(2015, 11, 11), "past");
        int days = calendarService.getCalendarInformationBetweenDates(
                LocalDate.now(),
                LocalDate.now())
                .getHolidays();

        assertEquals(days, 1);
    }

    //---CALCULATE ALL DAYS TESTS---------------------------------------------------------------------------------------

    @Test(expected = DateOrderException.class)
    public void calculateAllDaysEndBeforeBeginDate() {
        LocalDate begin = LocalDate.of(2016, 12, 31);
        LocalDate end = LocalDate.of(2016, 12, 1);
        calendarService.getCalendarInformationBetweenDates(begin, end).getAllDays();
    }

    @Test
    public void calculateAllDaysCorrectly() {
        LocalDate begin = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2016, 12, 31);
        int days = calendarService.getCalendarInformationBetweenDates(begin, end).getAllDays();

        assertEquals(days, 31);
    }

    @Test
    public void calculateAllDaysCorrectlyFebruary() {
        LocalDate begin = LocalDate.of(2017, 02, 1);
        LocalDate end = LocalDate.of(2017, 03, 1);
        int days = calendarService.getCalendarInformationBetweenDates(begin, end).getAllDays();

        assertEquals(days, 29);
    }

    //---GET CURRENT MONTH FULL TIME------------------------------------------------------------------------------------

    @Test
    public void getCurrentMonthFullDurationCorrectly() {
        LocalDate now = LocalDate.now();
        holidayService.createHoliday(now.minusDays(2), "holiday");

        LocalDate begin = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate end = LocalDate.of(now.getYear(), now.getMonth(), now.lengthOfMonth());
        CalendarDto dto = calendarService.getCalendarInformationBetweenDates(begin, end);

        assertNotEquals(dto.getWeekends(), 0);
        assertEquals(dto.getHolidays(), 1);
        assertTrue(dto.getAllDays() > 28);
        assertEquals(calendarService.getCalendarInformationBetweenDates(begin, end).getWorkdays(),
                (dto.getAllDays() - dto.getWeekends() - dto.getHolidays()));
    }
}
