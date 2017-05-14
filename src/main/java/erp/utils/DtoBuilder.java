package erp.utils;

import erp.domain.*;
import erp.dto.*;
import erp.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

        dto.setDay(report.getDate().getDayOfMonth());
        dto.setMonth(report.getDate().getMonth().name().substring(0,3));
        dto.setYear(report.getDate().getYear());
        return dto;
    }

    public static ProgressDto progressToDto(User user, double userActualHoursWorked, double userExpectedHoursWorked) {
        ProgressDto progressDto = new ProgressDto();
        progressDto.setUserId(user.getId());
        progressDto.setUserActualHoursWorked(userActualHoursWorked);
        progressDto.setUserExpectedHoursWorked(userExpectedHoursWorked);
        return progressDto;
    }

    public static HolidayDto holidayToDto(Holiday holiday) {
        HolidayDto dto = new HolidayDto();
        dto.setId(holiday.getId());
        dto.setDate(holiday.getDate());
        dto.setDescription(holiday.getDescription());
        return dto;
    }

    public static MissedDayDto missedDayToDto(MissedDay missedDay) {
        MissedDayDto dto = new MissedDayDto();
        dto.setId(missedDay.getId());
        dto.setDate(missedDay.getDate());
        dto.setType(missedDay.getMissedDayType().toString());
        return dto;
    }

    public static CalendarDto calendarToDto(int weekends, int holidays, int workdays, int allDays) {
        CalendarDto dto = new CalendarDto();
        dto.setWeekends(weekends);
        dto.setHolidays(holidays);
        dto.setWorkdays(workdays);
        dto.setAllDays(allDays);
        return dto;
    }

    public static TeamDto teamToDto(Team team, UserRepository userRepository) {
        List<BriefUserDto> userDtos = new ArrayList<>();
        TeamDto teamDto = new TeamDto();
        Iterator idsIterator = team.getUserIds();

        while (idsIterator.hasNext()) {
            Object userId = idsIterator.next();
            userDtos.add(createBriefUserDto(userRepository, (String) userId));
        }

        teamDto.setId(team.getId());
        teamDto.setName(team.getName());
        teamDto.setTeamLeadBriefUserDto(createBriefUserDto(userRepository, team.getTeamLeadId()));
        teamDto.setBriefUserDtos(userDtos);

        return teamDto;
    }

    private static BriefUserDto createBriefUserDto(UserRepository userRepository, String id) {
        String name = userRepository.findUserNameById(id);
        if (name.isEmpty())
            throw new RuntimeException("User hasn't found");

        BriefUserDto dto = new BriefUserDto();
        dto.setId(id);
        dto.setName(name);

        return dto;
    }
}
