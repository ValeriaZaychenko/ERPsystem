package erp.utils;

import erp.domain.Holiday;
import erp.domain.Report;
import erp.domain.User;
import erp.dto.HolidayDto;
import erp.dto.ProgressDto;
import erp.dto.ReportDto;
import erp.dto.UserDto;

import java.nio.charset.StandardCharsets;

public final class DtoBuilder {

    public static UserDto userToDto(User user) {
        UserDto dto =  new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setUserRole(user.getUserRole().toString());
        dto.setHashedPassword(new String(user.getHashedPassword(), StandardCharsets.UTF_8));
        return dto;
    }

    public static ReportDto reportToDto(Report report) {
        ReportDto dto = new ReportDto();
        dto.setId(report.getId());
        dto.setDate(report.getDate());
        dto.setDuration(report.getDuration());
        dto.setDescription(report.getDescription());
        dto.setUserId(report.getUser().getId());
        dto.setRemote(report.isRemote());
        return dto;
    }

    public static ProgressDto progressToDto(User user, double userWorkingTimeForMonth, double fullTimeBetweenDates) {
        ProgressDto progressDto = new ProgressDto();
        progressDto.setUserId(user.getId());
        progressDto.setUserName(user.getName());
        progressDto.setUserCurrentMonthWorkingTime(userWorkingTimeForMonth);
        progressDto.setProgress(userWorkingTimeForMonth * 100.0 / fullTimeBetweenDates);
        return progressDto;
    }

    public static HolidayDto holidayToDto(Holiday holiday) {
        HolidayDto dto = new HolidayDto();
        dto.setId(holiday.getId());
        dto.setDate(holiday.getDate());
        dto.setDescription(holiday.getDescription());
        return dto;
    }
}
