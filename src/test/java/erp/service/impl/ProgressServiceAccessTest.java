package erp.service.impl;

import erp.domain.User;
import erp.domain.UserRole;
import erp.service.IProgressService;
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
public class ProgressServiceAccessTest {

    @Inject
    private PasswordService passwordService;
    @Inject
    private IProgressService progressService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test(expected = AccessDeniedException.class)
    public void viewAllUsersWorkingTime() {
        progressService.getAllUsersProgressBetweenDates(LocalDate.now(), DateParser.parseDate("2020-04-12"));
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
