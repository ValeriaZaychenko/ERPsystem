package erp.utils;

import erp.domain.Report;
import erp.domain.User;
import erp.dto.ReportDto;
import erp.dto.UserDto;

import java.nio.charset.StandardCharsets;

public final class DtoBuilder {

    public static UserDto toDto(User user) {
        UserDto dto =  new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setUserRole(user.getUserRole().toString());
        dto.setHashedPassword(new String(user.getHashedPassword(), StandardCharsets.UTF_8));
        return dto;
    }

    public static ReportDto toDto(Report report) {
        ReportDto dto = new ReportDto();
        dto.setId(report.getId());
        dto.setDate(report.getDate());
        dto.setDuration(report.getDuration());
        dto.setDescription(report.getDescription());
        dto.setUserId(report.getUser().getId());
        dto.setRemote(report.isRemote());
        return dto;
    }
}
