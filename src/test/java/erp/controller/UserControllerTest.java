package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ViewNames;
import erp.domain.UserRole;
import erp.dto.UserDto;
import erp.service.IUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith( MockitoJUnitRunner.class )
public class UserControllerTest {

    @Mock
    private IUserService mockUserService;
    @InjectMocks
    private UserController theController;

    private MockMvc mockMvc;
    private String userId;
    private UserDto userDto;
    List<UserDto> dtos;

    @Before
    public void setup()
    {
        dtos = new ArrayList<>();

        userId = UUID.randomUUID().toString();

        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("Oleg");
        userDto.setEmail("Olegov");
        userDto.setUserRole("USER");

        this.mockMvc = MockMvcBuilders.standaloneSetup( theController ).build();
    }

    @Test
    public void noUsersInListYet() throws Exception {
        when( mockUserService.viewUsers()).thenReturn(dtos);

        this.mockMvc.perform(
                get("/users")
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.USER))
                .andExpect(model().attribute(AttributeNames.UserView.Users, dtos))
                .andExpect(model().attribute(AttributeNames.UserView.PossibleUserRoles, UserRole.values()));

        verify(mockUserService, times( 1 ))
                .viewUsers();
    }

    @Test
    public void createUser() throws Exception {
        this.mockMvc.perform(
                post("/users/add")
                        .param( "name", userDto.getName())
                        .param( "email", userDto.getEmail())
                        .param( "userRole", userDto.getUserRole())
        )
                .andExpect(status().isOk());

        verify(mockUserService, only())
                .createUser(
                        userDto.getName(),
                        userDto.getEmail(),
                        userDto.getUserRole());
    }

    @Test
    public void editUser() throws Exception {
        this.mockMvc.perform(
                post("/users/edit")
                        .param( "id", userId)
                        .param( "name", userDto.getName())
                        .param( "email", userDto.getEmail())
                        .param( "userRole", userDto.getUserRole())
        )
                .andExpect(status().isOk());

        verify(mockUserService, only())
                .editUser(
                        userId,
                        userDto.getName(),
                        userDto.getEmail(),
                        userDto.getUserRole());
    }

    @Test
    public void deleteUser() throws Exception {
        this.mockMvc.perform(
                post("/users/delete")
                        .param( "id", userId)
        )
                .andExpect(status().isOk());

        verify(mockUserService, only())
                .removeUser(userId);
    }
}

