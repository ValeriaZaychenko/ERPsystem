package erp.service.impl;

import erp.domain.User;
import erp.domain.UserRole;
import erp.service.IUserService;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional()
@WithMockUser(username = "user", authorities={"AUTH_USER"})
public class UserServiceAccessTest {

    @Inject
    private IUserService userService;
    @Inject
    private PasswordService passwordService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test(expected = AccessDeniedException.class)
    public void createUserFromUserRole() {
        userService.createUser("pet", "peter@mail.ru", "ADMIN");
    }

    @Test(expected = AccessDeniedException.class)
    public void editUserFromUserRole() {
        String id = create();
        userService.editUser(id, "Petya Petrovich", "p@mai", "USER");
    }

    @Test(expected = AccessDeniedException.class)
    public void deleteUserFromUserRole() {
        String id = create();
        userService.removeUser(id);
    }

    @Test(expected = AccessDeniedException.class)
    public void findUserFromUserRole() {
        String id = create();
        userService.findUserById(id);
    }

    @Test(expected = AccessDeniedException.class)
    public void viewUsersFromUserRole() {
        create();
        userService.viewUsers();
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
