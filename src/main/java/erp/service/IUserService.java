package erp.service;


import erp.domain.User;
import erp.dto.UserDto;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface IUserService {

    String createUser(
            @NotBlank String name,
            @Email String email,
            @NotNull String userRole);

    void changeUserName(
            @NotNull String id,
            @NotBlank String name);

    void changeUserEmail(
            @NotNull String id,
            @NotBlank String email);

    void changeUserRole(
            @NotNull String id,
            @NotNull String userRole);

    void removeUser(@NotNull  String id);

    User findUser(@NotNull String id);

    Iterable<UserDto> viewUsers();
}
