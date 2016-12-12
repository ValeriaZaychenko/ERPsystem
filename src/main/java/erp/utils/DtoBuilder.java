package erp.utils;

import erp.domain.Report;
import erp.domain.User;
import erp.dto.ReportDto;
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

    public static ReportDto toDto(Report report) {
        ReportDto dto = new ReportDto();
        dto.setId(report.getId());
        dto.setDate(report.getDate().toString());
        dto.setWorkingTime(report.getWorkingTime());
        dto.setDescription(report.getDescription());
        dto.setUserId(report.getUser().getId());
        return dto;
    }
}
