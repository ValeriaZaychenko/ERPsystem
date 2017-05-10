package erp.service.impl;

import erp.domain.Report;
import erp.domain.User;
import erp.dto.ProgressDto;
import erp.exceptions.EntityNotFoundException;
import erp.repository.ReportRepository;
import erp.repository.UserRepository;
import erp.service.ICalendarService;
import erp.service.IProgressService;
import erp.utils.DateOrderChecker;
import erp.utils.DtoBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressService implements IProgressService {

    @Inject
    private ReportRepository reportRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private ICalendarService calendarService;

    /*
    Count sum of user working time in range of dates include borders and return ProgressDto
     */
    @Transactional
    @Override
    public ProgressDto getUserProgressBetweenDates(String userId, LocalDate beginDate, LocalDate endDate) {
        DateOrderChecker.checkEndDateAfterBegin(beginDate, endDate);

        User user = restoreUserFromRepository(userId);
        double userWorkingTimeForMonth = 0.0;

        List<Report> userReports = reportRepository.findByUserAndBetweenQuery(beginDate, endDate, user);

        for(Report r : userReports) {
            userWorkingTimeForMonth += r.getDuration();
        }

        return DtoBuilder.progressToDto(
                user,
                userWorkingTimeForMonth,
                getFullTimeBetweenDates(beginDate, endDate));
    }

    /*
    Count sum of all users working time in range of dates include borders
    return list of ProgressDto sorted by progress reversed
     */
    @Transactional
    @Override
    public List<ProgressDto> getAllUsersProgressBetweenDates(LocalDate beginDate, LocalDate endDate) {
        DateOrderChecker.checkEndDateAfterBegin(beginDate, endDate);

        List<User> users = userRepository.findAll();
        List<ProgressDto> progressDtos = new ArrayList<>();

        for(User user : users) {
            progressDtos.add(getUserProgressBetweenDates(user.getId(), beginDate, endDate));
        }

        return progressDtos
                .stream()
                .sorted((p1, p2) -> Double.compare(p2.getProgress(), p1.getProgress()))
                .collect(Collectors.toList());
    }

    private User restoreUserFromRepository(String userId) {
        User user = userRepository.findOne(userId);
        if (user == null)
            throw new EntityNotFoundException(User.class.getName());

        return user;
    }

    private double getFullTimeBetweenDates(LocalDate begin, LocalDate end) {
        return calendarService.getCalendarInformationBetweenDates(begin, end).getWorkdays() * 8.00;
    }
}
