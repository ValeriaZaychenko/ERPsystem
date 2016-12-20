package erp.service.impl;

import erp.dto.ProgressDto;
import erp.dto.ReportDto;
import erp.exceptions.EntityNotFoundException;
import erp.exceptions.InvalidDateException;
import erp.service.IReportService;
import erp.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
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
        reportService.createReport(null, 0, null, null, "false");
    }

    @Test(expected = ConstraintViolationException.class)
    public void createReportBlankDate() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");

        reportService.createReport("", 2, "description", userId, "true");
    }

    @Test(expected = InvalidDateException.class)
    public void createReportInvalidDateFormat() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");

        reportService.createReport("2045-33-12", 2, "description", userId, "true");
    }

    @Test(expected = ConstraintViolationException.class)
    public void createReportInvalidTime() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");

        reportService.createReport("2015-11-11", 48, "description", userId, "true");
    }

    @Test(expected = ConstraintViolationException.class)
    public void createReportBlankDescription() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");

        reportService.createReport("2015-11-11", 8, "", userId, "true");
    }

    @Test(expected = ConstraintViolationException.class)
    public void createReportNullUserId() {
        userService.createUser("Petya", "ppp@mail.ru", "ADMIN");

        reportService.createReport("2015-11-11", 8, "description", null, "true");
    }

    @Test(expected = EntityNotFoundException.class)
    public void createReportUserNoExist() {
        reportService.createReport("2015-11-11", 8, "description", UUID.randomUUID().toString(), "true");
    }

    @Test
    public void createReportCorrectly() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        String reportId = reportService.createReport("2015-11-11", 8, "description", userId, "true");

        assertNotNull(reportId);

        ReportDto dto = reportService.findReport(reportId);

        assertEquals("2015-11-11", dto.getDate());
        assertEquals(8, dto.getWorkingTime());
        assertEquals("description", dto.getDescription());
        assertEquals(userId, dto.getUserId());
        assertEquals(dto.isRemote(), true);
    }

    @Test
    public void createTwoReportsOneUserCorrectly() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        reportService.createReport("2015-11-11", 8, "description", userId, "true");
        reportService.createReport("2016-11-11", 5, "Done: issue1", userId, "true");

        assertEquals(reportService.viewUserReports(userId).size(), 2);
    }

    @Test
    public void createThreeReportsTwoUsersCorrectly() {
        String userId1 = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        String userId2 = userService.createUser("Ivan", "ivan@mail.ru", "USER");

        reportService.createReport("2015-11-11", 8, "description", userId1, "true");
        reportService.createReport("2016-11-11", 5, "Done: issue1", userId1, "true");
        reportService.createReport("2016-11-10", 3, "Done: issue2", userId2, "true");

        assertEquals(reportService.viewAllReports().size(), 3);
    }

    @Test(expected = EntityNotFoundException.class)
    public void removeReportNoExist() {
        userService.createUser("Petya", "ppp@mail.ru", "ADMIN");

        reportService.removeReport(UUID.randomUUID().toString());
    }

    @Test(expected = ConstraintViolationException.class)
    public void removeReportNullId() {
        reportService.removeReport(null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void removeReportCorrectly() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        String reportId = reportService.createReport("2015-11-11", 8, "description", userId, "true");

        reportService.removeReport(reportId);

        reportService.findReport(reportId);
    }

    @Test(expected = ConstraintViolationException.class)
    public void editReportNullId() {
        reportService.editReport(null, "2015-11-11", 8, "description", "true");
    }

    @Test(expected = EntityNotFoundException.class)
    public void editReportNotExist() {
        reportService.editReport(UUID.randomUUID().toString(), "2015-11-11", 8, "description", "true");
    }

    @Test(expected = ConstraintViolationException.class)
    public void editReportBlankDate() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        String reportId = reportService.createReport("2015-11-11", 8, "description", userId, "true");

        reportService.editReport(reportId, "", 8, "description", "true");
    }

    @Test(expected = InvalidDateException.class)
    public void editReportIncorrectDate() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        String reportId = reportService.createReport("2015-11-11", 8, "description", userId, "true");

        reportService.editReport(reportId, "2016/12/13", 8, "description", "true");
    }

    @Test(expected = ConstraintViolationException.class)
    public void editReportInvalidWorkingTime() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        String reportId = reportService.createReport("2015-11-11", 8, "description", userId, "true");

        reportService.editReport(reportId, "2015-11-11", 40, "description", "true");
    }

    @Test(expected = ConstraintViolationException.class)
    public void editReportBlankDescription() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        String reportId = reportService.createReport("2015-11-11", 8, "description", userId, "true");

        reportService.editReport(reportId, "2015-11-11", 5, "", "true");
    }

    @Test
    public void editReportSameFields() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        String reportId = reportService.createReport("2015-11-11", 8, "description", userId, "true");

        reportService.editReport(reportId, "2015-11-11", 8, "description", "true");

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getDate(), "2015-11-11");
        assertEquals(dto.getWorkingTime(), 8);
        assertEquals(dto.getDescription(), "description");
    }

    @Test
    public void editReportDate() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        String reportId = reportService.createReport("2015-11-11", 8, "description", userId, "true");

        reportService.editReport(reportId, "2020-11-11", 8, "description", "true");

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getDate(), "2020-11-11");
    }

    @Test
    public void editReportWorkingTime() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        String reportId = reportService.createReport("2015-11-11", 8, "description", userId, "true");

        reportService.editReport(reportId, "2015-11-11", 10, "description", "true");

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getWorkingTime(), 10);
    }

    @Test
    public void editReportDescription() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        String reportId = reportService.createReport("2015-11-11", 8, "description", userId, "true");

        reportService.editReport(reportId, "2015-11-11", 10, "changed descritpion", "true");

        ReportDto dto = reportService.findReport(reportId);

        assertEquals(dto.getDescription(), "changed descritpion");
    }

    @Test
    public void editReportRemote() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        String reportId = reportService.createReport("2015-11-11", 8, "description", userId, "true");

        reportService.editReport(reportId, "2015-11-11", 10, "description", "false");

        ReportDto dto = reportService.findReport(reportId);

        assertFalse(dto.isRemote());
    }

    @Test(expected = ConstraintViolationException.class)
    public void viewUserProgressNullId() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        reportService.createReport("2016-12-12", 8, "description", userId, "true");

        reportService.getUserCurrentMonthWorkingTime(null);
    }

    @Test
    public void viewUserProgressCorrectly() {
        String userId = userService.createUser("Petya", "ppp@mail.ru", "ADMIN");
        reportService.createReport("2016-12-12", 8, "description", userId, "true");
        reportService.createReport("2016-12-12", 7, "description", userId, "true");

        ProgressDto progress = reportService.getUserCurrentMonthWorkingTime(userId);

        assertEquals(progress.getUserCurrentMonthWorkingTime(), 15.0,  0.1);
        assertEquals(progress.getProgress(), 15.0 * 100 / 160, 0.1);
    }
}
