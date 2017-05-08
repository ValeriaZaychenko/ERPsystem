package erp.service.impl;

import erp.domain.Report;
import erp.domain.User;
import erp.dto.ReportDto;
import erp.event.RemoveUserEvent;
import erp.exceptions.EntityNotFoundException;
import erp.exceptions.WorkloadIncompatibilityException;
import erp.repository.ReportRepository;
import erp.repository.UserRepository;
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
import java.util.stream.Stream;

@Service
public class ReportService implements IReportService, ApplicationListener<RemoveUserEvent> {

    @Inject
    private ReportRepository reportRepository;
    @Inject
    private UserRepository userRepository;

    @Transactional
    @Override
    public String createReport(LocalDate date, double duration, String description, String userId, boolean remote) {
        checkDurationOfReportsLess24(countDurationsSum(userId, date), duration);

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

        if(durationModified) {
            checkDurationOfReportsLess24(
                    countDurationsSum(report.getUser().getId(), date) - report.getDuration(),
                    duration);
            report.setDuration(duration);
        }

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

        return DtoBuilder.reportToDto(report);
    }

    @Transactional
    @Override
    public List<ReportDto> viewAllReports() {
        List<Report> reports = reportRepository.findAll();

        return getReportDtosFromReportsList(reports);
    }

    @Transactional
    @Override
    public List<ReportDto> viewUserReportsBetweenDates(String userId, LocalDate begin, LocalDate end) {
        DateOrderChecker.checkEndDateAfterBegin(begin, end);

        User user = restoreUserFromRepository(userId);
        List<Report> userReports = reportRepository.findByUserAndBetweenQuery(begin, end, user);

        return getReportDtosFromReportsList(userReports);
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
            reportDtos.add(DtoBuilder.reportToDto(r));
        }

        return reportDtos;
    }

    private double countDurationsSum(String userId, LocalDate date) {
        try (Stream<Report> stream = reportRepository.findAllByCustomQueryNotNull(userId, date)) {
            return stream
                    .map(Report::getDuration)
                    .reduce(
                            0.0,
                            (a, b) -> a + b);
        }
    }

    private void checkDurationOfReportsLess24(double sumDurations, double duration) {
        if (sumDurations + duration > 24.0)
            throw new WorkloadIncompatibilityException(Double.toString(duration));
    }

    @Override
    public void onApplicationEvent(RemoveUserEvent event) {
        reportRepository.removeByUserIdEquals(event.getUserId());
    }
}
