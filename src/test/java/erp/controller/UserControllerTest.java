package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ViewNames;
import erp.domain.User;
import erp.domain.UserRole;
import erp.dto.ProgressDto;
import erp.dto.UserDto;
import erp.exceptions.DuplicateEmailException;
import erp.exceptions.EntityNotFoundException;
import erp.exceptions.UnknownRoleException;
import erp.service.IReportService;
import erp.service.IUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private IUserService mockUserService;
    @Mock
    private IReportService mockReportService;

    @InjectMocks
    private UserController theController;

    private MockMvc mockMvc;
    private String userId;
    private UserDto userDto;
    private List<UserDto> dtos;
    private ProgressDto progressDto;
    private List<ProgressDto> progressDtos;

    @Before
    public void setup() {
        dtos = new ArrayList<>();
        progressDtos = new ArrayList<>();

        userId = UUID.randomUUID().toString();

        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("Oleg");
        userDto.setEmail("Olegov");
        userDto.setUserRole("USER");

        progressDto = new ProgressDto();
        progressDto.setUserId(userId);
        progressDto.setUserName(userDto.getName());
        progressDto.setUserCurrentMonthWorkingTime(16.0);
        progressDto.setProgress(30.0);

        progressDtos.add(progressDto);

        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setSuffix(".jsp");


        this.mockMvc = MockMvcBuilders.standaloneSetup(theController)
                .setControllerAdvice(new ExceptionHandlingAdvice())
                .setViewResolvers(resolver)
                .build();
    }

    @Test
    public void noUsersInListYet() throws Exception {
        when(mockUserService.viewUsers()).thenReturn(dtos);

        this.mockMvc.perform(
                get("/users")
       )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.USER.user))
                .andExpect(model().attribute(AttributeNames.UserViewUsers.users, dtos))
                .andExpect(model().attribute(AttributeNames.UserViewPossibleUserRoles.possibleUserRoles, UserRole.values()))
        ;

        verify(mockUserService, times(1))
                .viewUsers()
        ;
    }

    @Test
    public void createUser() throws Exception {
        this.mockMvc.perform(
                post("/users/add")
                        .param("name", userDto.getName())
                        .param("email", userDto.getEmail())
                        .param("userRole", userDto.getUserRole())
       )
                .andExpect(status().isOk())
        ;

        verify(mockUserService, only())
                .createUser(
                        userDto.getName(),
                        userDto.getEmail(),
                        userDto.getUserRole()
               );
    }

    @Test
    public void createUserDuplicateEmail() throws Exception {
        doThrow(new DuplicateEmailException(userDto.getEmail()))
                .when(mockUserService).
                createUser(userDto.getName(), userDto.getEmail(), userDto.getUserRole());

        this.mockMvc.perform(
                post("/users/add")
                        .param("name", userDto.getName())
                        .param("email", userDto.getEmail())
                        .param("userRole", userDto.getUserRole())
        )
                .andExpect(status().is4xxClientError())
                .andExpect(view().name(ViewNames.ERROR.errorPlain))
                .andExpect(new ResultMatcher() {

                    @Override
                    public void match(MvcResult result) throws Exception {
                        result.getResponse().getContentAsString().contains("Database already has user with email");
                    }
                });
    }

    @Test
    public void createUserUnknownRole() throws Exception {
        doThrow(new UnknownRoleException(userDto.getUserRole()))
                .when(mockUserService).
                createUser(userDto.getName(), userDto.getEmail(), userDto.getUserRole());

        this.mockMvc.perform(
                post("/users/add")
                        .param("name", userDto.getName())
                        .param("email", userDto.getEmail())
                        .param("userRole", userDto.getUserRole())
        )
                .andExpect(view().name("error"))
                .andExpect(new ResultMatcher() {

                    @Override
                    public void match(MvcResult result) throws Exception {
                        result.getResponse().getContentAsString().contains("Can't parse role from");
                    }
                });
    }

    @Test
    public void editUser() throws Exception {
        this.mockMvc.perform(
                post("/users/edit")
                        .param("id", userId)
                        .param("name", userDto.getName())
                        .param("email", userDto.getEmail())
                        .param("userRole", userDto.getUserRole())
       )
                .andExpect(status().isOk())
        ;

        verify(mockUserService, only())
                .editUser(
                        userId,
                        userDto.getName(),
                        userDto.getEmail(),
                        userDto.getUserRole()
               );
    }

    @Test
    public void editUserDuplicateEmail() throws Exception {
        doThrow(new DuplicateEmailException(userDto.getEmail()))
                .when(mockUserService).
                editUser(userId, userDto.getName(), userDto.getEmail(), userDto.getUserRole());

        this.mockMvc.perform(
                post("/users/edit")
                        .param("id", userId)
                        .param("name", userDto.getName())
                        .param("email", userDto.getEmail())
                        .param("userRole", userDto.getUserRole())
        )
                .andExpect(view().name(ViewNames.ERROR.errorPlain))
                .andExpect(new ResultMatcher() {

                    @Override
                    public void match(MvcResult result) throws Exception {
                        result.getResponse().getContentAsString().contains("Database already has user with email");
                    }
                });
    }

    @Test
    public void editUserUnknownRole() throws Exception {
        doThrow(new UnknownRoleException("BBB"))
                .when(mockUserService).
                editUser(userId, userDto.getName(), userDto.getEmail(),"BBB");

        this.mockMvc.perform(
                post("/users/edit")
                        .param("id", userId)
                        .param("name", userDto.getName())
                        .param("email", userDto.getEmail())
                        .param("userRole", "BBB")
        )
                .andExpect(view().name("error"))
                .andExpect(new ResultMatcher() {

                    @Override
                    public void match(MvcResult result) throws Exception {
                        result.getResponse().getContentAsString().contains("Can't parse role from");
                    }
                });
    }

    @Test
    public void deleteUser() throws Exception {
        this.mockMvc.perform(
                post("/users/delete")
                        .param("id", userId)
       )
                .andExpect(
                        status().isOk()
               );

        verify(mockUserService, only())
                .removeUser(userId);
    }

    @Test
    public void deleteUserNotFound() throws Exception {
        doThrow(new EntityNotFoundException(User.class.getName()))
                .when(mockUserService).
                removeUser(userId);

        this.mockMvc.perform(
                post("/users/delete")
                        .param("id", userId)
        )
                .andExpect(view().name("error"))
                .andExpect(new ResultMatcher() {

                    @Override
                    public void match(MvcResult result) throws Exception {
                        result.getResponse().getContentAsString().contains("Database doesn't have entity with name");
                    }
                });
    }

    @Test
    public void viewProgress() throws Exception {
        when(mockReportService.
                getAllUsersWorkingTimeBetweenDates(LocalDate.of(2016, 12, 1), LocalDate.now()))
                        .thenReturn(progressDtos);

        this.mockMvc.perform(
                get("/users/progress")
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.PROGRESS.progress))
                .andExpect(model().attribute(AttributeNames.UserViewUsers.progress, progressDtos)
                );

        verify(mockReportService, times(1))
                .getAllUsersWorkingTimeBetweenDates(LocalDate.of(2016, 12, 1), LocalDate.now())
        ;
    }
}

