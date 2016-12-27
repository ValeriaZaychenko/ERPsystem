package erp.service;


import erp.validation.Past;
import erp.validation.PastOrToday;
import erp.dto.ProgressDto;
import erp.dto.ReportDto;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Validated
public interface IReportService {

    String createReport(
            @PastOrToday LocalDate date,
            @Min(value = 0)@Max(value = 24) double duration,
            @NotBlank String description,
            @NotNull String userId,
            boolean remote);

    void editReport(
            @NotNull String id,
            @PastOrToday LocalDate date,
            @Min(value = 0) @Max(value = 24) double duration,
            @NotBlank String description,
            boolean remote);

    void removeReport(@NotNull String id);

    ReportDto findReport(@NotNull String id);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    List<ReportDto> viewAllReports();

    List<ReportDto> viewUserReports(@NotNull String userId);

    double getCurrentMonthFullTime();

    ProgressDto getUserWorkingTimeBetweenDates(
            @NotNull String userId,
            @Past LocalDate beginDate,
            @PastOrToday LocalDate endDate);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    List<ProgressDto> getAllUsersWorkingTimeBetweenDates(
            @Past LocalDate beginDate,
            @PastOrToday LocalDate endDate);
}
