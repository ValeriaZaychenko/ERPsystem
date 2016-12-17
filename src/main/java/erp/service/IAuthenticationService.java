package erp.service;


import erp.dto.UserDto;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

@Validated
public interface IAuthenticationService extends AuthenticationProvider {

    @Override
    UserDto authenticate(Authentication authentication);
}
