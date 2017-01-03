package erp.service.impl;

import erp.domain.Report;
import erp.domain.User;
import erp.dto.ProgressDto;
import erp.dto.ReportDto;
import erp.event.RemoveUserEvent;
import erp.exceptions.EntityNotFoundException;
import erp.repository.ReportRepository;
import erp.repository.UserRepository;
import erp.service.IDayCounterService;
import erp.service.IReportService;
import erp.utils.DateOrderChecker;
import erp.utils.DtoBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService implements IReportService, ApplicationListener<RemoveUserEvent> {

    @Inject
    private ReportRepository reportRepository;
    @Inject
    private UserRepository userRepository;

    @Inject
    private IDayCounterService dayCounterService;

    @Transactional
    @Override
    public String createReport(LocalDate date, double duration, String description, String userId, boolean remote) {
        User user = restoreUserFromRepository(userId);

        Report report = new Report(date, duration, description, user, remote);

        reportRepository.save(report);
        return report.getId();
    }

    @Transactional
    @Override
    public void editReport(String id, LocalDate date, double duration, String description, boolean remote) {
        Report report = restoreReportFromRepository(id);

        boolean dateModified = false;
        boolean durationModified = false;
        boolean descriptionModified = false;
        boolean remoteModified = false;

        if(!report.getDate().equals(date))
            dateModified = true;

        if(report.getDuration() != duration)
            durationModified = true;

        if(!report.getDescription().equals(description))
            descriptionModified = true;

        if(report.isRemote() != remote)
            remoteModified = true;

        if (!(dateModified || durationModified || descriptionModified || remoteModified))
            return; //No modification detected

        if(dateModified)
            report.setDate(date);

        if(durationModified)
            report.setDuration(duration);

        if(descriptionModified)
            report.setDescription(description);

        if (remoteModified)
            report.setRemote(remote);
    }

    /*
    Delete from repository by entity just
    because throwing custom runtime exception easier then catching
    EmptyResultDataAccessException from CRUDRepository
     */
    @Transactional
    @Override
    public void removeReport(String id) {
        Report report = restoreReportFromRepository(id);
        reportRepository.delete(report);
    }

    @Transactional
    @Override
    public ReportDto findReport(String id) {
        Report report = restoreReportFromRepository(id);

        return DtoBuilder.toDto(report);
    }

    @Transactional
    @Override
    public List<ReportDto> viewAllReports() {
        List<Report> reports = reportRepository.findAll();

        return getReportDtosFromReportsList(reports);
    }

    @Transactional
    @Override
    public List<ReportDto> viewUserReports(String userId) {
        User user = restoreUserFromRepository(userId);
        List<Report> userReports = reportRepository.findByUserOrderByDateDesc(user);

        return getReportDtosFromReportsList(userReports);
    }

    public double getFullTimeBetweenDates(LocalDate begin, LocalDate end) {
        int allDays = dayCounterService.getAllDaysQuantityBetweenDates(begin, end);
        int weekendDays = dayCounterService.countWeekendsBetweenDates(begin, end);

        return (allDays - weekendDays) * 8.00;
    }

    /*
    Count sum of user working time in range of dates include borders and return ProgressDto
     */
    @Transactional
    @Override
    public ProgressDto getUserWorkingTimeBetweenDates(String userId, LocalDate beginDate, LocalDate endDate) {
        DateOrderChecker.checkEndDateAfterBegin(beginDate, endDate);

        User user = restoreUserFromRepository(userId);
        double userWorkingTimeForMonth = 0.0;

        List<Report> userReports = reportRepository.findByUserAndBetweenQuery(beginDate, endDate, user);

        for(Report r : userReports) {
            userWorkingTimeForMonth += r.getDuration();
        }

        ProgressDto progressDto = new ProgressDto();
        progressDto.setUserId(userId);
        progressDto.setUserName(user.getName());
        progressDto.setUserCurrentMonthWorkingTime(userWorkingTimeForMonth);
        progressDto.setProgress(userWorkingTimeForMonth * 100.0 / getFullTimeBetweenDates(beginDate, endDate));

        return progressDto;
    }

    /*
    Count sum of all users working time in range of dates include borders
    return list of ProgressDto sorted by progress reversed
     */
    @Transactional
    @Override
    public List<ProgressDto> getAllUsersWorkingTimeBetweenDates(LocalDate beginDate, LocalDate endDate) {
        DateOrderChecker.checkEndDateAfterBegin(beginDate, endDate);

        List<User> users = userRepository.findAll();
        List<ProgressDto> progressDtos = new ArrayList<>();

        for(User user : users) {
            progressDtos.add(getUserWorkingTimeBetweenDates(user.getId(), beginDate, endDate));
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

    private Report restoreReportFromRepository(String id) {
        Report report = reportRepository.findOne(id);
        if (report == null)
            throw new EntityNotFoundException(Report.class.getName());

        return report;
    }

    private List<ReportDto> getReportDtosFromReportsList(List<Report> reports) {
        List<ReportDto> reportDtos = new ArrayList<>();

        for(Report r : reports) {
            reportDtos.add(DtoBuilder.toDto(r));
        }

        return reportDtos;
    }

    @Override
    public void onApplicationEvent(RemoveUserEvent event) {
        reportRepository.removeByUserIdEquals(event.getUserId());
    }
}
