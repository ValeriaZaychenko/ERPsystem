package erp.service.impl;

import erp.domain.Report;
import erp.domain.User;
import erp.dto.ProgressDto;
import erp.dto.ReportDto;
import erp.exceptions.BooleanParseException;
import erp.exceptions.EntityNotFoundException;
import erp.exceptions.InvalidDateException;
import erp.repository.ReportRepository;
import erp.repository.UserRepository;
import erp.service.IReportService;
import erp.utils.DtoBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService implements IReportService {

    @Inject
    private ReportRepository reportRepository;
    @Inject
    private UserRepository userRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transactional
    @Override
    public String createReport(String date, int workingTime, String description, String userId, String remote) {

        User user = restoreUserFromRepository(userId);
        LocalDate localDate = parseDate(date);

        Report report = new Report(localDate, workingTime, description, user, parseRemote(remote));

        reportRepository.save(report);
        return report.getId();
    }

    @Transactional
    @Override
    public void editReport(String id, String date, int workingTime, String description, String remote) {
        Report report = restoreReportFromRepository(id);

        boolean dateModified = false;
        boolean workingTimeModified = false;
        boolean descriptionModified = false;
        boolean remoteModified = false;

        LocalDate localDate = null;
        boolean r = parseRemote(remote);

        if(!report.getDate().toString().equals(date))
            dateModified = true;

        if(report.getWorkingTime() != workingTime)
            workingTimeModified = true;

        if(!report.getDescription().equals(description))
            descriptionModified = true;

        if(report.isRemote() != r)
            remoteModified = true;

        if (!(dateModified || workingTimeModified || descriptionModified || remoteModified))
            return; //No modification detected

        if (dateModified)
            localDate = parseDate(date);

        if(dateModified)
            report.setDate(localDate);

        if(workingTimeModified)
            report.setWorkingTime(workingTime);

        if(descriptionModified)
            report.setDescription(description);

        if (remoteModified)
            report.setRemote(r);
    }

    private boolean parseRemote(String remote) {
        try {
            boolean r = Boolean.valueOf(remote);
            return r;
        } catch (IllegalArgumentException e) {
            throw new BooleanParseException();
        }
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
        List<ReportDto> reportDtos = new ArrayList<>();

        for(Report r : reports) {
            reportDtos.add(DtoBuilder.toDto(r));
        }

        return reportDtos;
    }

    @Transactional
    @Override
    public List<ReportDto> viewUserReports(String userId) {
        //TODO Change this. Query like "FROM reports SELECT reports r WHERE r.user.id = userId
        //now its looking by entity too damn big complexity
        User user = restoreUserFromRepository(userId);
        List<Report> userReports = reportRepository.findByUser(user);

        List<ReportDto> reportDtos = new ArrayList<>();

        for(Report r : userReports) {
            reportDtos.add(DtoBuilder.toDto(r));
        }

        return reportDtos;
    }

    @Override
    public double getCurrentMonthFullTime() {
        return 160.0;
    }

    @Transactional
    @Override
    public ProgressDto getUserCurrentMonthWorkingTime(@NotNull String userId) {
        User user = restoreUserFromRepository(userId);
        double userWorkingTimeForMonth = 0.0;

        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        int maxDay = LocalDate.now().lengthOfMonth();
        LocalDate beginDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, maxDay);

        List<Report> userReports = reportRepository.findByUserAndBetweenQuery(beginDate, endDate, user);

        for(Report r : userReports) {
            userWorkingTimeForMonth += r.getWorkingTime();
        }

        ProgressDto progressDto = new ProgressDto();
        progressDto.setUserId(userId);
        progressDto.setUserName(user.getName());
        progressDto.setUserCurrentMonthWorkingTime(userWorkingTimeForMonth);
        progressDto.setProgress(userWorkingTimeForMonth * 100.0 / getCurrentMonthFullTime());

        return progressDto;
    }

    @Transactional
    @Override
    public List<ProgressDto> getAllUsersCurrentMonthWorkingTime() {
        List<User> users = userRepository.findAll();
        List<ProgressDto> progressDtos = new ArrayList<>();

        for(User user : users) {
            progressDtos.add(getUserCurrentMonthWorkingTime(user.getId()));
        }
        return progressDtos;
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

    private LocalDate parseDate(String date) {
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(date, formatter);
            return localDate;
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(date);
        }
    }
}
