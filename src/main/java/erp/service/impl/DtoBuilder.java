package erp.service.impl;

import erp.domain.User;
import erp.dto.UserDto;

/**
 * Created by lera on 12/2/2016.
 */
public final class DtoBuilder {

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserRole().toString());
    }
}
