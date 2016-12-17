package erp.service.impl;

import erp.domain.User;
import erp.dto.UserDto;
import erp.service.IAuthenticationService;
import erp.service.IMailService;
import erp.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional()
@WithMockUser(username = "ram", authorities={"AUTH_ADMIN"})
public class UserServiceTest {

    @Inject
    private IUserService userService;
    @Inject
    private IMailService mailService;
    @Inject
    private PasswordService passwordService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void serviceShouldNotBeNull() {
        assertNotNull(userService);
    }

    @Test
    public void serviceDontHaveUsersByDefault() {
        assertTrue(userService.viewUsers().isEmpty());
    }

    @Test(expected = Exception.class)
    public void createUserNullFields() {
        userService.createUser(null, null, null);
    }

    @Test(expected = Exception.class)
    public void createUserBlankName() {
        userService.createUser("", "peter", "USER");
    }

    @Test(expected = Exception.class)
    public void createUserInvalidEmail() {
        userService.createUser("pet", "peter", "USER");
    }

    @Test(expected = Exception.class)
    public void createUserBlankEmail() {
        userService.createUser("pet", "", "USER");
    }

    @Test(expected = Exception.class)
    public void createUserNullEmail() {
        userService.createUser("pet", null, "USER");
    }

    @Test(expected = Exception.class)
    public void createUserNullUserRole() {
        userService.createUser("pet", "peter@mail.ru", null);
    }

    @Test
    public void createUserCorrectly() {
        String id = userService.createUser("pet", "peter@mail.ru", "ADMIN");

        assertNotNull(id);

        UserDto userdto = (userService.findUserById(id));

        assertEquals("pet", userdto.getName());
        assertEquals("peter@mail.ru", userdto.getEmail());
        assertEquals("ADMIN", userdto.getUserRole());
        assertEquals(1, userService.viewUsers().size());

        User user = findUserById(id);

        assertNotNull(user.getHashedPassword());
    }

    @Test(expected = Exception.class)
    public void createUserSameEmail() {
        String email = "peter@mail.ru";

        userService.createUser("pet", email, "ADMIN");
        userService.createUser("pet", email, "ADMIN");
    }

    @Test(expected = Exception.class)
    public void createUserInvalidRoleString() {
        userService.createUser("pet", "ddd@dd", "invalid");
    }

    @Test
    public void createTwoUsersCorrectly() {
        createSimpleUser();
        createSecondSimpleUser();

        assertEquals(2, userService.viewUsers().size());
    }

    @Test(expected = Exception.class)
    public void deleteUserFromEmptyService() {
        userService.removeUser(UUID.randomUUID().toString());
    }

    @Test(expected = Exception.class)
    public void deleteUserNullId() {
        userService.removeUser(null);
    }

    @Test(expected = Exception.class)
    public void deleteUserCorrectly() {
        String id0 = createSimpleUser();
        String id1 = createSecondSimpleUser();

        userService.removeUser(id0);

        assertEquals(1, userService.viewUsers().size());
        assertNotNull(userService.findUserById(id1));
        userService.findUserById(id0);
    }

    @Test(expected = Exception.class)
    public void deleteUserTwice() {
        String id = createSimpleUser();

        userService.removeUser(id);
        userService.removeUser(id);
    }

    @Test(expected = Exception.class)
    public void editNullIdUser() {
        createSimpleUser();

        userService.editUser(null, "name", "email@email", "USER");
    }

    @Test(expected = Exception.class)
    public void editBlankNameUser() {
        String id = createSimpleUser();

        userService.editUser(id, "", "peter@mail.ru", "ADMIN");
    }

    @Test(expected = Exception.class)
    public void editBlankEmailUser() {
        String id = createSimpleUser();

        userService.editUser(id, "newName", "", "ADMIN");
    }

    @Test(expected = Exception.class)
    public void editInvalidEmailUser() {
        String id = createSimpleUser();

        userService.editUser(id, "newName", "invalid", "ADMIN");
    }

    @Test
    public void editNameCorrectly() {
        String id = createSimpleUser();
        userService.editUser(id, "New", "peter@mail.ru", "ADMIN");
        User user = findUserById(id);

        assertEquals(user.getName(), "New");
    }

    @Test
    public void editEmailCorrectly() {
        String id = createSimpleUser();
        userService.editUser(id, "pet", "new@mail.ru", "ADMIN");
        User user = findUserById(id);

        assertEquals(user.getEmail(), "new@mail.ru");
    }

    @Test(expected = Exception.class)
    public void editEmailToNonUnique() {
        String id0 = createSimpleUser();
        createSimpleUser();

        userService.editUser(id0, "pet", "cater@mail.ru", "ADMIN");
    }

    @Test
    public void editRoleCorrectly(){
        String id = createSimpleUser();
        userService.editUser(id, "pet", "peter@mail.ru", "USER");
        User user = findUserById(id);

        assertEquals(user.getUserRole().toString(), "USER");
    }

    @Test(expected = Exception.class)
    public void editRoleInvalidUserRoleString(){
        String id = createSimpleUser();
        userService.editUser(id, "pet", "peter@mail.ru", "invalid");
    }

    @Test
    public void editAllFieldsToSameValue() {
        String id = createSimpleUser();
        userService.editUser(id, "pet", "peter@mail.ru", "ADMIN");
        UserDto dto = userService.findUserById(id);

        assertEquals(dto.getName(), "pet");
        assertEquals(dto.getEmail(), "peter@mail.ru");
        assertEquals(dto.getUserRole(), "ADMIN");
    }

    @Test(expected = Exception.class)
    public void changePasswordNullId() {
        createSimpleUser();
        userService.changePassword(null, "1234", "111");
    }

    @Test(expected = Exception.class)
    public void changeEmptyOldPassword() {
        String id = createSimpleUser();
        userService.changePassword(id, "", "111");
    }

    @Test(expected = Exception.class)
    public void changeEmptyNewPassword() {
        String id = createSimpleUser();
        userService.changePassword(id, "111", "");
    }

    @Test(expected = Exception.class)
    public void changePasswordOldNoMatch() {
        String id = createSimpleUser();
        userService.changePassword(id, "11111111", "333");
    }

    @Test
    public void changePasswordCorrectly() {
        String id = createSimpleUser();
        String password = "333";
        userService.changePassword(id, mailService.getLastContent(), password);

        User user = findUserById(id);

        assertTrue(passwordService.comparePasswords("333", user.getHashedPassword()));
    }

    private String createSimpleUser() {
        return userService.createUser("pet", "peter@mail.ru", "ADMIN");
    }

    private String createSecondSimpleUser() {
        return userService.createUser("cat", "cater@mail.ru", "USER");
    }

    private User findUserById(String id) {
        TypedQuery<User> query =  entityManager.createQuery(
                "SELECT e FROM employees e WHERE e.id = '" + id + "'",
                User.class);

        return query.getSingleResult();
    }
}
