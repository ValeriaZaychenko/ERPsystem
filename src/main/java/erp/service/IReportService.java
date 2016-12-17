package erp.service;


import erp.dto.ReportDto;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface IReportService {

    String createReport(
            @NotBlank @DateTimeFormat(pattern = "yyyy-MM-dd") String date,
            @Max(value = 24) int workingTime,
            @NotBlank String description,
            @NotNull String userId);

    void editReport(
            @NotNull String id,
            @NotBlank @DateTimeFormat(pattern = "yyyy-MM-dd") String date,
            @Max(value = 24) int workingTime,
            @NotBlank String description);

    void removeReport(@NotNull String id);

    ReportDto findReport(@NotNull String id);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    List<ReportDto> viewAllReports();

    List<ReportDto> viewUserReports(@NotNull String userId);
}
