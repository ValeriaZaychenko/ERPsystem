package erp.service.impl;

import erp.dto.ProgressDto;
import erp.exceptions.DateOrderException;
import erp.exceptions.EntityNotFoundException;
import erp.service.*;
import erp.utils.DateParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional
@WithMockUser(username = "ram", authorities={"AUTH_ADMIN"})
public class ProgressServiceTest {

    @Inject
    private IProgressService progressService;
    @Inject
    private ICalendarService calendarService;
    @Inject
    private IHolidayService holidayService;
    @Inject
    private IUserService userService;
    @Inject
    private IReportService reportService;

    //---GET USER WORKING TIME BETWEEN DATES VALIDATION TESTS-----------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void viewUserProgressNullId() {
        String userId = createSimpleUser();
        reportService.createReport(get2015_11_11(), 8, "description", userId, true);

        progressService.getUserProgressBetweenDates(null, get2015_11_11().minusDays(1), get2015_11_11().plusDays(1));
    }

    @Test(expected = ConstraintViolationException.class)
    public void viewUserProgressFutureBeginDate() {
        String userId = createSimpleUser();
        reportService.createReport(get2020_11_11(), 8, "description", userId, true);

        progressService.getUserProgressBetweenDates(userId, get2020_11_11(), get2015_11_11());
    }

    @Test(expected = ConstraintViolationException.class)
    public void viewUserProgressFutureEndDate() {
        String userId = createSimpleUser();
        reportService.createReport(get2020_11_11(), 8, "description", userId, true);

        progressService.getUserProgressBetweenDates(userId, get2015_11_11(), get2020_11_11());
    }

    //---GET USER WORKING TIME BETWEEN DATES LOGIC TESTS----------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void viewUserProgressInvalidId() {
        String userId = createSimpleUser();
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                8, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                7, "description", userId, true);

        LocalDate begin = DateParser.parseDate("2016-10-10");
        LocalDate end = DateParser.parseDate("2016-12-12");

        progressService.getUserProgressBetweenDates(UUID.randomUUID().toString(), begin, end);
    }

    @Test
    public void viewUserProgressCorrectlyPastEndDate() {
        String userId = createSimpleUser();
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                8, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                7, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2016-12-24"),
                7, "description", userId, true);

        holidayService.createHoliday(LocalDate.of(2016, 11,11), "Holiday");

        LocalDate begin = DateParser.parseDate("2016-10-10");
        LocalDate end = DateParser.parseDate("2016-12-12");

        ProgressDto progress = progressService.getUserProgressBetweenDates(userId, begin, end);
        double expectedHours = calendarService.getCalendarInformationBetweenDates(begin, end).getWorkdays() * 8.00;

        assertEquals(progress.getUserActualHoursWorked(), 15.0,  0.1);
        assertEquals(progress.getProgress(), (15.0 / expectedHours) * 100, 0.1);
    }

    @Test
    public void viewUserProgressCorrectlyTodayEndDate() {
        String userId = createSimpleUser();
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                8, "description", userId, true);
        reportService.createReport(LocalDate.now(),
                7, "description", userId, true);

        holidayService.createHoliday(LocalDate.of(2016, 11,11), "Holiday");

        LocalDate begin = DateParser.parseDate("2016-10-10");

        ProgressDto progress = progressService.getUserProgressBetweenDates(userId, begin, LocalDate.now());
        double expectedHours = calendarService.getCalendarInformationBetweenDates(begin, LocalDate.now()).getWorkdays() * 8.00;

        assertEquals(progress.getUserActualHoursWorked(), 15.0,  0.1);
        assertEquals(progress.getProgress(), (15.0 / expectedHours) * 100, 0.1);
    }

    @Test
    public void viewUserProgressIncludeFirstDate() {
        String userId = createSimpleUser();

        reportService.createReport(DateParser.parseDate("2016-11-01"),
                8, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                7, "description", userId, true);

        holidayService.createHoliday(LocalDate.of(2016, 11,11), "Holiday");

        LocalDate begin = DateParser.parseDate("2016-11-01");
        LocalDate end = DateParser.parseDate("2016-12-12");

        ProgressDto progress = progressService.getUserProgressBetweenDates(userId, begin, end);
        double expectedHours = calendarService.getCalendarInformationBetweenDates(begin, end).getWorkdays() * 8.0;

        assertEquals(progress.getUserActualHoursWorked(), 15.0,  0.1);
        assertEquals(progress.getProgress(),
                (15.0 / expectedHours) * 100, 0.1);
    }

    @Test
    public void viewUserProgressIncludeLastDate() {
        String userId = createSimpleUser();

        reportService.createReport(DateParser.parseDate("2016-11-11"),
                8, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2016-11-12"),
                7, "description", userId, true);

        holidayService.createHoliday(LocalDate.of(2016, 11,11), "Holiday");

        LocalDate begin = DateParser.parseDate("2016-11-01");
        LocalDate end = DateParser.parseDate("2016-11-12");

        ProgressDto progress = progressService.getUserProgressBetweenDates(userId, begin, end);
        double expectedHours = calendarService.getCalendarInformationBetweenDates(begin, end).getWorkdays() * 8.0;

        assertEquals(progress.getUserActualHoursWorked(), 15.0,  0.1);
        assertEquals(progress.getProgress(),
                (15.0 / expectedHours) * 100, 0.1);
    }

    @Test(expected = DateOrderException.class)
    public void viewUserProgressEndDateBeforeBeginDate() {
        String userId = createSimpleUser();
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                8, "description", userId, true);
        reportService.createReport(LocalDate.now(),
                7, "description", userId, true);

        LocalDate begin = DateParser.parseDate("2016-12-12");
        LocalDate end = DateParser.parseDate("2016-10-10");

        progressService.getUserProgressBetweenDates(userId, begin, end);
    }

    //---GET ALL USERS WORKING TIME BETWEEN DATES VALIDATION TESTS------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void viewAllUsersProgressFutureBeginDate() {
        progressService.getAllUsersProgressBetweenDates(LocalDate.now().plusDays(1), get2015_11_11());
    }

    //---GET ALL USERS WORKING TIME BETWEEN DATES LOGIC TESTS-----------------------------------------------------------

    @Test
    public void viewAllUsersProgressCorrectly() {
        String userId = createSimpleUser();
        String userId2 = userService.createUser("Ivan", "ivanov@mail.ru", "ADMIN");

        reportService.createReport(DateParser.parseDate("2016-11-11"),
                8, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2016-10-10"),
                8, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2016-11-12"),
                7, "description", userId2, true);

        LocalDate begin = DateParser.parseDate("2016-09-09");
        LocalDate end = DateParser.parseDate("2016-11-12");

        List<ProgressDto> dtos = progressService.getAllUsersProgressBetweenDates(begin, end);
        double expectedHours = calendarService.getCalendarInformationBetweenDates(begin, end).getWorkdays() * 8.0;

        assertEquals(dtos.size(), 2);
        assertEquals(dtos.get(0).getProgress(), (16.0 / expectedHours) * 100, 0.1);
        assertEquals(dtos.get(1).getProgress(), (7.0 / expectedHours) * 100, 0.1);
    }

    @Test(expected = DateOrderException.class)
    public void viewAllUsersProgressEndDateBeforeBeginDate() {
        createSimpleUser();
        LocalDate begin = DateParser.parseDate("2016-12-12");
        LocalDate end = DateParser.parseDate("2016-11-11");

        progressService.getAllUsersProgressBetweenDates(begin, end);
    }

    private LocalDate get2015_11_11() {
        return DateParser.parseDate("2015-11-11");
    }

    private LocalDate get2020_11_11() {
        return DateParser.parseDate("2020-11-11");
    }

    private String createSimpleUser() {
        return userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
    }
}
