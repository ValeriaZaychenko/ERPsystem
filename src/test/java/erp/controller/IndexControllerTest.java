package erp.controller;

import erp.controller.constants.ErrorKeys;
import erp.controller.constants.SessionKeys;
import erp.controller.constants.ViewNames;
import erp.dto.UserDto;
import erp.exceptions.MismatchPasswordException;
import erp.service.IAuthenticationService;
import erp.service.IUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.UUID;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class IndexControllerTest {

    @Mock
    private IUserService mockUserService;

    @Mock
    private IAuthenticationService mockAuthService;

    @InjectMocks
    private IndexController theController;

    private MockMvc mockMvc;
    private String userId;
    private UserDto userDto;

    @Before
    public void setup() {
        userId = UUID.randomUUID().toString();

        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("Oleg");
        userDto.setEmail("Olegov");
        userDto.setUserRole("USER");

        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setSuffix(".jsp");

        this.mockMvc = MockMvcBuilders.standaloneSetup(theController)
                .setControllerAdvice(new ExceptionHandlingAdvice())
                .setViewResolvers(resolver).build();
    }

    @Test
    public void returnHomePage() throws Exception {
        this.mockMvc.perform(
                get("/")
      )
                .andExpect(redirectedUrl("/home"))
        ;
    }

    @Test
    public void accessDenied() throws Exception {
        this.mockMvc.perform(
                get("/accessDenied")
        )
                .andExpect(view().name("error"))
        ;
    }

    @Test
    public void returnHomePageForUserToHome() throws Exception {
        when(mockUserService.
                needToChangePassword(userDto))
                .thenReturn(false);

        this.mockMvc.perform(
                get("/home")
                .principal(userDto)
        )
                .andExpect(redirectedUrl("/reports"))
        ;
    }

    @Test
    public void returnHomePageForUserSecondTime() throws Exception {
        when(mockUserService.
                needToChangePassword(userDto))
                .thenReturn(true);

        this.mockMvc.perform(
                get("/home")
                        .principal(userDto)
        )
                .andExpect(redirectedUrl("/changePassword/"))
        ;
    }

    @Test
    public void returnHomePageForAdmin() throws Exception {
        userDto.setUserRole("ADMIN");

        this.mockMvc.perform(
                get("/home")
                        .principal(userDto)
        )
                .andExpect(redirectedUrl("/users"))
        ;
    }

    @Test
    public void setRightLocaleRus() throws Exception {
        this.mockMvc.perform(
                post("/setlocale/")
                        .param("language", "ru")
       )
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute(
                        SessionKeys.LOCALE.locale,
                        is(allOf(
                                hasProperty("language", is("ru")),
                                hasProperty("country", is("RU"))
                       ))));
    }

    @Test
    public void setDefaultLocaleEng() throws Exception {
        this.mockMvc.perform(
                post("/setlocale/")
                        .param("language", "illegallanguage")
       )
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute(
                        SessionKeys.LOCALE.locale,
                        is(allOf(
                                hasProperty("language", is("en"))
                       ))));
    }

    @Test
    public void loginRedirected() throws Exception {
        this.mockMvc.perform(
                get("/login")
        )
        .andExpect(view().name(ViewNames.LOGIN.login))
        ;
    }

    @Test
    public void getChangePasswordView() throws Exception {
        this.mockMvc.perform(
                get("/changePassword/")
       )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.SETTINGS.settings))
        ;
    }

    @Test(expected = Exception.class)
    public void changePasswordWrongData() throws Exception {
        Mockito.doThrow(new RuntimeException("Invalid data"))
                .when(mockUserService).changePassword(userId, "111", "12345");

        this.mockMvc.perform(
                post("/changePassword/")
                .param("userId", userId)
                .param("oldPassword", "111")
                .param("newPassword", "12345")
                .param("newPasswordConfirmed", "12345")
       );

        verify(mockUserService, only())
                .changePassword(
                        userId,
                        "111",
                        "12345"
               );
    }

    @Test
    public void changePasswordMismatchOld() throws Exception {
        doThrow(new MismatchPasswordException())
                .when(mockUserService).changePassword(userId, "111", "12345");

        this.mockMvc.perform(
                post("/changePassword/")
                        .param("userId", userId)
                        .param("oldPassword", "111")
                        .param("newPassword", "12345")
                        .param("newPasswordConfirmed", "12345")
        )
        .andExpect(view().name("error"))
                .andExpect( (result) ->
                        result.getResponse().getContentAsString().contains(ErrorKeys.MismatchPasswordMessage)
                );
    }

    @Test
    public void changePasswordCorrectly() throws Exception {
        this.mockMvc.perform(
                post("/changePassword/")
                        .param("userId", userId)
                        .param("oldPassword", "111")
                        .param("newPassword", "12345")
                        .param("newPasswordConfirmed", "12345")
       )
                .andExpect(redirectedUrl("/"));

        verify(mockUserService, only())
                .changePassword(
                        userId,
                        "111",
                        "12345"
               );
    }

}
