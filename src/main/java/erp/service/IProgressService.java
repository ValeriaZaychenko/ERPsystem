package erp.service;

import erp.dto.ProgressDto;
import erp.validation.DateBorder;
import erp.validation.Past;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

//TODO teams progress
@Validated
public interface IProgressService {

    double getFullTimeBetweenDates(
            LocalDate beginDate,
            LocalDate endDate);

    ProgressDto getUserProgressBetweenDates(
            @NotNull String userId,
            @Past(value = DateBorder.INCLUDE_TODAY) LocalDate beginDate,
            LocalDate endDate);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    List<ProgressDto> getAllUsersProgressBetweenDates(
            @Past(value = DateBorder.INCLUDE_TODAY) LocalDate beginDate,
            LocalDate endDate);
}
