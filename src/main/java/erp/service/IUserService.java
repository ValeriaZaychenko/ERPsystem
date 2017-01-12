package erp.service;


import erp.dto.UserDto;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface IUserService {

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    String createUser(
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotNull String userRole);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    void editUser(
            @NotNull String id,
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotNull String userRole);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    void removeUser(@NotNull  String id);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    UserDto findUserById(@NotNull String id);

    void changePassword(
            @NotNull String id,
            @NotEmpty String oldPassword,
            @NotEmpty String newPassword);

    boolean needToChangePassword(UserDto dto);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    List<UserDto> viewUsers();
}
