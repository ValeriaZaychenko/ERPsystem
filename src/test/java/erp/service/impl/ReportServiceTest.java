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
    public void serviceShouldNotBeNull() {
        assertNotNull(reportService);
    }

    @Test
    public void serviceDontHaveReportByDefault() {
        assertTrue(reportService.viewAllReports().isEmpty());
    }

    @Test(expected = ConstraintViolationException.class)
    public void createReportNullFields() {
        reportService.createReport(null, 0, null, null, false);
    }

    @Test(expected = InvalidDateException.class)
    public void createReportBlankDate() {
        String userId = createSimpleUser();

        reportService.createReport(DateParser.parseDate(""),
                2, "description", userId, true);
    }

    @Test(expected = InvalidDateException.class)
    public void createReportInvalidDateFormat() {
        String userId = createSimpleUser();

        reportService.createReport(DateParser.parseDate("2045-33-12"),
                2, "description", userId, true);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createReportInvalidTime() {
        String userId = createSimpleUser();

        reportService.createReport(DateParser.parseDate("2015-11-11"),
                48, "description", userId, true);
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

    @Test(expected = EntityNotFoundException.class)
    public void createReportUserNoExist() {
        reportService.createReport(get2015_11_11(),
                8, "description", UUID.randomUUID().toString(), true);
    }

    @Test
    public void createReportCorrectly() {
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

    @Test(expected = EntityNotFoundException.class)
    public void removeReportNoExist() {
        createSimpleUser();

        reportService.removeReport(UUID.randomUUID().toString());
    }

    @Test(expected = ConstraintViolationException.class)
    public void removeReportNullId() {
        reportService.removeReport(null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void removeReportCorrectly() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.removeReport(reportId);

        reportService.findReport(reportId);
    }

    @Test(expected = ConstraintViolationException.class)
    public void editReportNullId() {
        reportService.editReport(null, get2015_11_11(),
                8, "description", true);
    }

    @Test(expected = EntityNotFoundException.class)
    public void editReportNotExist() {
        reportService.editReport(UUID.randomUUID().toString(),
                get2015_11_11(), 8, "description", true);
    }

    @Test(expected = InvalidDateException.class)
    public void editReportBlankDate() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, DateParser.parseDate(""),
                8, "description", true);
    }

    @Test(expected = InvalidDateException.class)
    public void editReportIncorrectDate() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, DateParser.parseDate("2016/12/13"),
                8, "description", true);
    }

    @Test(expected = ConstraintViolationException.class)
    public void editReportInvalidWorkingTime() {
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
    public void editReportDate() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, get2020_11_11(),
                8, "description", true);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getDate(), get2020_11_11());
    }

    @Test
    public void editReportWorkingTime() {
        String userId = createSimpleUser();
        String reportId = reportService.createReport(get2015_11_11(),
                8, "description", userId, true);

        reportService.editReport(reportId, get2015_11_11(),
                10, "description", true);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getDuration(), 10, 0.1);
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

    @Test(expected = ConstraintViolationException.class)
    public void viewUserProgressNullId() {
        String userId = createSimpleUser();
        reportService.createReport(DateParser.parseDate("2016-12-12"), 8, "description", userId, true);

        reportService.getUserWorkingTimeBetweenDates(null, null, null);
    }

    @Test
    public void viewUserProgressCorrectly() {
        String userId = createSimpleUser();
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                8, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2016-11-11"),
                7, "description", userId, true);
        reportService.createReport(DateParser.parseDate("2017-11-11"),
                7, "description", userId, true);

        LocalDate begin = DateParser.parseDate("2016-10-10");
        LocalDate end = DateParser.parseDate("2016-12-12");

        ProgressDto progress = reportService.getUserWorkingTimeBetweenDates(userId, begin, end);

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
        reportService.createReport(DateParser.parseDate("2017-11-12"),
                7, "description", userId2, true);

        LocalDate begin = DateParser.parseDate("2016-09-09");
        LocalDate end = DateParser.parseDate("2016-11-12");

        List<ProgressDto> dtos = reportService.getAllUsersWorkingTimeBetweenDates(begin, end);

        assertEquals(dtos.size(), 2);
        assertEquals(dtos.get(0).getProgress(), 16.0 * 100/ 160, 0.1);
        assertEquals(dtos.get(1).getProgress(), 7.0 * 100/ 160, 0.1);
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
