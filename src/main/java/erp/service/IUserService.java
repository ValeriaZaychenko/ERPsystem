package erp.service;


import erp.dto.UserDto;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface IUserService {

    String createUser(
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotNull String userRole);

    void editUser(
            @NotNull String id,
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotNull String userRole);

    void removeUser(@NotNull  String id);

    UserDto findUser(@NotNull String id);

    void changePassword(
            @NotNull String id,
            @NotEmpty String oldPassword,
            @NotEmpty String newPassword);

    List<UserDto> viewUsers();

    UserDto authenticate(@NotEmpty String userLogin, @NotEmpty String password);
}
