package erp.service.impl;

import erp.dto.ProgressDto;
import erp.dto.ReportDto;
import erp.exceptions.EntityNotFoundException;
import erp.exceptions.InvalidDateException;
import erp.service.IReportService;
import erp.service.IUserService;
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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional
@WithMockUser(username = "ram", authorities={"AUTH_ADMIN"})
public class ReportServiceTest {

    @Inject
    private IReportService reportService;
    @Inject
    private IUserService userService;

    @Test
    public void serviceDontHaveReportByDefault() {
        assertTrue(reportService.viewAllReports().isEmpty());
    }

    //---CREATE REPORT VALIDATION TESTS---------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void createReportNullFields() {
        reportService.createReport(null, 0, null, null, false);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createReportFuture() {
        String userId = createSimpleUser();

        reportService.createReport(DateParser.parseDate("2045-10-12"),
                2, "description", userId, true);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createReportInvalidDurationMoreMax() {
        String userId = createSimpleUser();

        reportService.createReport(DateParser.parseDate("2015-11-11"),
                48, "description", userId, true);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createReportInvalidDurationLessMin() {
        String userId = createSimpleUser();

        reportService.createReport(DateParser.parseDate("2015-11-11"),
                -1, "description", userId, true);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createReportInvalidDuration() {
        String userId = createSimpleUser();

        reportService.createReport(DateParser.parseDate("2015-11-11"),
                Double.NaN, "description", userId, true);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createReportBlankDescription() {
        String userId = createSimpleUser();

        reportService.createReport(DateParser.parseDate("2015-11-11"),
                8, "", userId, true);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createReportNullUserId() {
        createSimpleUser();

        reportService.createReport(DateParser.parseDate("2015-11-11"),
                8, "description", null, true);
    }

    //---CREATE REPORT LOGIC TESTS--------------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void createReportUserNoExist() {
        reportService.createReport(get2015_11_11(),
                8, "description", UUID.randomUUID().toString(), true);
    }

    @Test
    public void createReportCorrectlyPastDate() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        assertNotNull(reportId);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(get2015_11_11(), dto.getDate());
        assertEquals(8, dto.getDuration(), 0.1);
        assertEquals("description", dto.getDescription());
        assertEquals(userId, dto.getUserId());
        assertEquals(dto.isRemote(), true);
    }

    @Test
    public void createReportCorrectlyToday() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(LocalDate.now(),
                8, "description", userId, true);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(LocalDate.now(), dto.getDate());
    }

    @Test
    public void createReportCorrectlyZeroDuration() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                0, "description", userId, true);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(0, dto.getDuration(), 0.1);
    }

    @Test
    public void createReportCorrectlyMaxDuration() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                24, "description", userId, true);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(24, dto.getDuration(), 0.1);
    }

    @Test
    public void createTwoReportsOneUserCorrectly() {
        String userId = createSimpleUser();
        reportService.createReport(get2015_11_11(),
                8, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                5, "Done: issue1", userId, true);

        assertEquals(reportService.viewUserReports(userId).size(), 2);
    }

    @Test
    public void createThreeReportsTwoUsersCorrectly() {
        String userId1 = createSimpleUser();
        String userId2 = userService.createUser("Ivan", "ivan@mail.ru", "USER");

        reportService.createReport(get2015_11_11(),
                8, "description", userId1, true);
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                5, "Done: issue1", userId1, true);
        reportService.createReport(DateParser.parseDate("2016-11-10"),
                3, "Done: issue2", userId2, true);

        assertEquals(reportService.viewAllReports().size(), 3);
    }

    //---EDIT REPORT VALIDATION TESTS-----------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void editReportNullId() {
        reportService.editReport(null, get2015_11_11(),
                8, "description", true);
    }

    @Test(expected = ConstraintViolationException.class)
    public void editReportFutureDate() {
        reportService.editReport(null, LocalDate.now().plusDays(1),
                8, "description", true);
    }

    @Test(expected = InvalidDateException.class)
    public void editReportBlankDate() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, DateParser.parseDate(""),
                8, "description", true);
    }

    @Test(expected = ConstraintViolationException.class)
    public void editReportInvalidWorkingDurationLessMin() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, get2015_11_11(),
                -8, "description", true);
    }

    @Test(expected = ConstraintViolationException.class)
    public void editReportInvalidWorkingDurationMoreMax() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, get2015_11_11(),
                40, "description", true);
    }

    @Test(expected = ConstraintViolationException.class)
    public void editReportBlankDescription() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, get2015_11_11(),
                5, "", true);
    }

    //---EDIT REPORT LOGIC TESTS----------------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void editReportNotExist() {
        reportService.editReport(UUID.randomUUID().toString(),
                get2015_11_11(), 8, "description", true);
    }

    @Test
    public void editReportSameFields() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11()
                , 8, "description", userId, true);

        reportService.editReport(reportId, get2015_11_11(),
                8, "description", true);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getDate(), get2015_11_11());
        assertEquals(dto.getDuration(), 8, 0.1);
        assertEquals(dto.getDescription(), "description");
    }

    @Test
    public void editReportDateToPast() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, LocalDate.now().minusDays(3),
                8, "description", true);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getDate(),  LocalDate.now().minusDays(3));
    }

    @Test
    public void editReportDateToToday() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, LocalDate.now(),
                8, "description", true);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getDate(), LocalDate.now());
    }

    @Test
    public void editReportWorkingDuration() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, get2015_11_11(),
                10, "description", true);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getDuration(), 10, 0.1);
    }

    @Test
    public void editReportWorkingDurationToZero() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, get2015_11_11(),
                0, "description", true);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getDuration(), 0, 0.1);
    }

    @Test
    public void editReportWorkingDurationToMax() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, get2015_11_11(),
                24, "description", true);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getDuration(), 24, 0.1);
    }

    @Test
    public void editReportDescription() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, get2015_11_11(),
                10, "changed description", true);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getDescription(), "changed description");
    }

    @Test
    public void editReportRemote() {
        String userId =  createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, get2015_11_11(),
                10, "description", false);

        ReportDto dto = reportService.findReport(reportId);

        assertFalse(dto.isRemote());
    }

    //---REMOVE REPORT VALIDATION TESTS---------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void removeReportNullId() {
        reportService.removeReport(null);
    }

    //---REMOVE REPORT LOGIC TESTS--------------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void removeReportNoExist() {
        createSimpleUser();

        reportService.removeReport(UUID.randomUUID().toString());
    }

    @Test(expected = EntityNotFoundException.class)
    public void removeReportCorrectly() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.removeReport(reportId);

        reportService.findReport(reportId);
    }

    //FIND REPORT VALIDATION TESTS--------------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void findUserNullId() {
        reportService.findReport(null);
    }

    //FIND REPORT LOGIC TESTS-------------------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void findUserInvalidId() {
        reportService.findReport(UUID.randomUUID().toString());
    }

    @Test
    public void findUserCorrectly() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        ReportDto dto = reportService.findReport(reportId);

        assertNotNull(dto);
        assertEquals(dto.getId(), reportId);
        assertEquals(dto.getDate(), get2015_11_11());
        assertEquals(dto.getDuration(), 8, 0.1);
        assertEquals(dto.getUserId(), userId);
    }

    //VIEW ALL REPORTS LOGIC TESTS--------------------------------------------------------------------------------------

    @Test
    public void viewAllReportsDontHaveAny() {
        List<ReportDto> dtos = reportService.viewAllReports();

        assertEquals(dtos.size(), 0);
    }

    @Test
    public void viewAllReportsCorrectEntity() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        List<ReportDto> dtos = reportService.viewAllReports();

        assertEquals(dtos.size(), 1);
        assertEquals(dtos.get(0).getId(), reportId);
        assertEquals(dtos.get(0).getDate(), get2015_11_11());
        assertEquals(dtos.get(0).getDuration(), 8, 0.1);
        assertEquals(dtos.get(0).getDescription(), "description");
        assertEquals(dtos.get(0).getUserId(), userId);
    }

    @Test
    public void viewAllReports() {
        String userId = createSimpleUser();
        reportService.createReport(get2015_11_11(),
                8, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                5, "Done: issue1", userId, true);

        List<ReportDto> dtos = reportService.viewAllReports();

        assertEquals(dtos.size(), 2);
    }

    //---VIEW USER REPORTS VALIDATION TESTS-----------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void viewUserReportNullId() {
        reportService.viewUserReports(null);
    }

    //---VIEW USER REPORTS LOGIC TESTS----------------------------------------------------------------------------------

    @Test
    public void viewUserReportDontHaveAny() {
        String userId = createSimpleUser();
        List<ReportDto> dtos = reportService.viewUserReports(userId);

        assertEquals(dtos.size(), 0);
    }

    @Test
    public void viewUserReportCorrectly() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        List<ReportDto> dtos = reportService.viewUserReports(userId);

        assertEquals(dtos.size(), 1);
        assertEquals(dtos.get(0).getId(), reportId);
        assertEquals(dtos.get(0).getDate(), get2015_11_11());
        assertEquals(dtos.get(0).getDuration(), 8, 0.1);
        assertEquals(dtos.get(0).getDescription(), "description");
        assertEquals(dtos.get(0).getUserId(), userId);
    }

    //---GET CURRENT MONTH FULL TIME------------------------------------------------------------------------------------

    @Test
    public void getCurrentMonthFullDurationConst() {
        assertEquals(reportService.getCurrentMonthFullTime(), 160, 0.1);
    }

    //---GET USER WORKING TIME BETWEEN DATES VALIDATION TESTS-----------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void viewUserProgressNullId() {
        String userId = createSimpleUser();
        reportService.createReport(get2015_11_11(), 8, "description", userId, true);

        reportService.getUserWorkingTimeBetweenDates(null, get2015_11_11().minusDays(1), get2015_11_11().plusDays(1));
    }

    @Test(expected = ConstraintViolationException.class)
    public void viewUserProgressFutureBeginDate() {
        String userId = createSimpleUser();
        reportService.createReport(get2020_11_11(), 8, "description", userId, true);

        reportService.getUserWorkingTimeBetweenDates(userId, get2020_11_11(), get2015_11_11());
    }

    @Test(expected = ConstraintViolationException.class)
    public void viewUserProgressFutureEndDate() {
        String userId = createSimpleUser();
        reportService.createReport(get2020_11_11(), 8, "description", userId, true);

        reportService.getUserWorkingTimeBetweenDates(userId, get2015_11_11(), get2020_11_11());
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

        reportService.getUserWorkingTimeBetweenDates(UUID.randomUUID().toString(), begin, end);
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

        LocalDate begin = DateParser.parseDate("2016-10-10");
        LocalDate end = DateParser.parseDate("2016-12-12");

        ProgressDto progress = reportService.getUserWorkingTimeBetweenDates(userId, begin, end);

        assertEquals(progress.getUserCurrentMonthWorkingTime(), 15.0,  0.1);
        assertEquals(progress.getProgress(), 15.0 * 100 / 160, 0.1);
    }

    @Test
    public void viewUserProgressCorrectlyTodayEndDate() {
        String userId = createSimpleUser();
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                8, "description", userId, true);
        reportService.createReport(LocalDate.now(),
                7, "description", userId, true);

        LocalDate begin = DateParser.parseDate("2016-10-10");

        ProgressDto progress = reportService.getUserWorkingTimeBetweenDates(userId, begin, LocalDate.now());

        assertEquals(progress.getUserCurrentMonthWorkingTime(), 15.0,  0.1);
        assertEquals(progress.getProgress(), 15.0 * 100 / 160, 0.1);
    }

    @Test
    public void viewUserProgressIncludeFirstDate() {
        String userId = createSimpleUser();

        reportService.createReport(DateParser.parseDate("2016-11-01"),
                8, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                7, "description", userId, true);

        LocalDate begin = DateParser.parseDate("2016-11-01");
        LocalDate end = DateParser.parseDate("2016-12-12");

        ProgressDto progress = reportService.getUserWorkingTimeBetweenDates(userId, begin, end);

        assertEquals(progress.getUserCurrentMonthWorkingTime(), 15.0,  0.1);
        assertEquals(progress.getProgress(), 15.0 * 100 / 160, 0.1);
    }

    @Test
    public void viewUserProgressIncludeLastDate() {
        String userId = createSimpleUser();

        reportService.createReport(DateParser.parseDate("2016-11-11"),
                8, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2016-11-12"),
                7, "description", userId, true);

        LocalDate begin = DateParser.parseDate("2016-11-01");
        LocalDate end = DateParser.parseDate("2016-11-12");

        ProgressDto progress = reportService.getUserWorkingTimeBetweenDates(userId, begin, end);

        assertEquals(progress.getUserCurrentMonthWorkingTime(), 15.0,  0.1);
        assertEquals(progress.getProgress(), 15.0 * 100 / 160, 0.1);
    }

    //---GET ALL USERS WORKING TIME BETWEEN DATES VALIDATION TESTS------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void viewAllUsersProgressFutureBeginDate() {
        reportService.getAllUsersWorkingTimeBetweenDates(LocalDate.now().plusDays(1), get2015_11_11());
    }

    @Test(expected = ConstraintViolationException.class)
    public void viewAllUsersProgressFutureEndDate() {
        reportService.getAllUsersWorkingTimeBetweenDates(get2015_11_11(), LocalDate.now().plusDays(1));
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

        List<ProgressDto> dtos = reportService.getAllUsersWorkingTimeBetweenDates(begin, end);

        assertEquals(dtos.size(), 2);
        assertEquals(dtos.get(0).getProgress(), 16.0 * 100/ 160, 0.1);
        assertEquals(dtos.get(1).getProgress(), 7.0 * 100/ 160, 0.1);
    }

    //TODO tests for past, past or today
    //do find methods on tests from repository

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
