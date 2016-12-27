package erp.service.impl;

import erp.domain.User;
import erp.domain.UserRole;
import erp.service.IReportService;
import erp.utils.DateParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional
@WithMockUser(username = "new", authorities={"AUTH_USER"})
public class ReportServiceAccessTest {

    @Inject
    private IReportService reportService;
    @Inject
    private PasswordService passwordService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test(expected = AccessDeniedException.class)
    public void viewAllUsersReports() {
        String id = create();
        reportService.createReport(LocalDate.now(), 2, "description", id, false);

        reportService.viewAllReports();
    }

    @Test(expected = AccessDeniedException.class)
    public void viewAllUsersWorkingTime() {
        reportService.getAllUsersWorkingTimeBetweenDates(LocalDate.now(), DateParser.parseDate("2020-04-12"));
    }

    private String create() {
        User user = new User(
                "Petya",
                "p@mail",
                UserRole.USER,
                passwordService.getHashFromPassword(passwordService.generatePassword())
        );

        entityManager.persist(user);

        return user.getId();
    }
}
