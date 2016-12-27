package erp.service.impl;

import erp.domain.User;
import erp.domain.UserRole;
import erp.dto.UserDto;
import erp.service.IAuthenticationService;
import erp.service.IPasswordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional
public class AuthentificationServiceTest {

    @Inject
    private IAuthenticationService authentificationService;
    @Inject
    private IPasswordService passwordService;

    @PersistenceContext
    private EntityManager entityManager;

    private static final String USER_NAME = "John";
    private static final String USER_EMAIL = "john.smith@gmail.com";
    private static final String USER_PASSWORD = "12345";

    @Before
    public void configureUsers ()
    {
        entityManager.persist(
            new User(
                    USER_NAME,
                    USER_EMAIL,
                    UserRole.USER,
                    passwordService.getHashFromPassword(USER_PASSWORD)
            )
        );
    }


    @Test
    public void authenticateEmptyLogin() {
        UserDto dto = authentificationService.authenticate(authToken("", "dddd"));
        assertNull(dto);
    }

    @Test
    public void authenticateEmptyPassword() {
        UserDto dto = authentificationService.authenticate(authToken("ddd", ""));
        assertNull(dto);
    }

    @Test
    public void authenticateUserIncorrectLogin() {
        UserDto dto = authentificationService.authenticate(authToken("new", "dddd"));
        assertNull(dto);
    }

    @Test
    public void authenticateUserIncorrectPassword() {
        UserDto dto = authentificationService.authenticate(authToken(USER_EMAIL, "dddd"));
        assertNull(dto);
    }

    @Test
    public void authenticateUserCorrectlyUserRole() {
        UserDto dto = authentificationService.authenticate(authToken(USER_EMAIL, USER_PASSWORD));

        assertEquals(USER_NAME, dto.getName());
        assertEquals(USER_EMAIL, dto.getEmail());
        assertTrue(
                passwordService.comparePasswords(
                        USER_PASSWORD,
                        dto.getHashedPassword().getBytes()
                )
        );
    }

    @Test
    public void authenticateUserCorrectlyAdminRole() {
        String USER_NAME = "Admin";
        String USER_EMAIL = "admin.smith@gmail.com";
        String USER_PASSWORD = "12345";

        entityManager.persist(
                new User(
                        USER_NAME,
                        USER_EMAIL,
                        UserRole.ADMIN,
                        passwordService.getHashFromPassword(USER_PASSWORD)
                )
        );

        UserDto dto = authentificationService.authenticate(authToken(USER_EMAIL, USER_PASSWORD));

        assertEquals(USER_NAME, dto.getName());
        assertEquals(USER_EMAIL, dto.getEmail());
        assertTrue(
                passwordService.comparePasswords(
                        USER_PASSWORD,
                        dto.getHashedPassword().getBytes()
                )
        );
    }

    private Authentication authToken (String email, String password) {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

}
