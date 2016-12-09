package erp.utils;

import erp.domain.User;
import erp.dto.UserDto;

public final class DtoBuilder {

    public static UserDto toDto(User user) {
        UserDto dto =  new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setUserRole(user.getUserRole().toString());
        return dto;
    }
}
