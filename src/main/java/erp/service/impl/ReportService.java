package erp.service.impl;

import erp.domain.Report;
import erp.domain.User;
import erp.dto.ReportDto;
import erp.repository.ReportRepository;
import erp.repository.UserRepository;
import erp.service.IReportService;
import erp.utils.DtoBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public String createReport(String date, int workingTime, String description, String userId) {

        User user = restoreUserFromRepository(userId);
        LocalDate localDate = LocalDate.parse(date, formatter);//TODO exceptions

        Report report = new Report(localDate, workingTime, description, user);

        reportRepository.save(report);
        return report.getId();
    }

    @Transactional
    @Override
    public void editReport(String id, String date, int workingTime, String description) {
        Report report = restoreReportFromRepository(id);

        boolean dateModified = false;
        boolean workingTimeModified = false;
        boolean descriptionModified = false;

        LocalDate localDate = null;

        if(!report.getDate().toString().equals(date))
            dateModified = true;

        if(report.getWorkingTime() != workingTime)
            workingTimeModified = true;

        if(!report.getDescription().equals(description))
            descriptionModified = true;

        if (!(dateModified || workingTimeModified || descriptionModified))
            return; //No modification detected

        if (dateModified)
             localDate = LocalDate.parse(date, formatter);//TODO exceptions

        if(dateModified)
            report.setDate(localDate);

        if(workingTimeModified)
            report.setWorkingTime(workingTime);

        if(descriptionModified)
            report.setDescription(description);
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

    private User restoreUserFromRepository(String userId) {
        User user = userRepository.findOne(userId);
        if (user == null)
            throw new RuntimeException("Database doesn't have this user");

        return user;
    }

    private Report restoreReportFromRepository(String id) {
        Report report = reportRepository.findOne(id);
        if (report == null)
            throw new RuntimeException("Database doesn't have this report");

        return report;
    }
}
