package erp.controller;

import erp.controller.constants.SessionKeys;
import erp.controller.constants.ViewNames;
import erp.dto.UserDto;
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

import java.util.UUID;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith( MockitoJUnitRunner.class )
public class IndexControllerTest {

    @Mock
    private IUserService mockUserService;
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

        this.mockMvc = MockMvcBuilders.standaloneSetup( theController ).build();
    }

    @Test
    public void returnHomePage() throws Exception {
        this.mockMvc.perform(
                get("/")
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.HOME.home))
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
                        is( allOf(
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
                        is( allOf(
                                hasProperty("language", is("en"))
                        ))));
    }

    @Test
    public void loginFailed() throws Exception {
        when( mockUserService.authenticate(userDto.getEmail(), "12345"))
                .thenThrow(new RuntimeException("Login failed"))
        ;

        this.mockMvc.perform(
                post("/login/")
                .param("userLogin", userDto.getEmail())
                .param("password", "12345")
        )
                .andExpect(redirectedUrl("/"))
        ;

        verify(mockUserService, only())
                .authenticate(
                        userDto.getEmail(),
                       "12345"
                );
    }

    @Test
    public void loginSucceed() throws Exception {
        when( mockUserService.authenticate(userDto.getEmail(), "12345")).thenReturn(userDto);

        this.mockMvc.perform(
                post("/login/")
                        .param("userLogin", userDto.getEmail())
                        .param("password", "12345")
        )
                .andExpect(redirectedUrl("/users/"))
                .andExpect(request().sessionAttribute(SessionKeys.USER.user, userDto));

        verify(mockUserService, only())
                .authenticate(
                        userDto.getEmail(),
                        "12345"
                );
    }

    @Test
    public void logout() throws Exception {
        this.mockMvc.perform(
                post("/logout/")
        )
                .andExpect(redirectedUrl("/"))
                .andExpect(request().sessionAttribute(SessionKeys.USER.user, is(nullValue())));
    }

    @Test
    public void getChangePasswordView() throws Exception {
        this.mockMvc.perform(
                get("/changePassword/")
        )
                .andExpect(status().isOk())
                .andExpect(view().name( ViewNames.SETTINGS.settings))
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
        );

        verify(mockUserService, only())
                .changePassword(
                        userId,
                        "111",
                        "12345"
                );
    }

    @Test
    public void changePasswordCorrectly() throws Exception {
        this.mockMvc.perform(
                post("/changePassword/")
                        .param("userId", userId)
                        .param("oldPassword", "111")
                        .param("newPassword", "12345")
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
