package erp.service;

import erp.dto.HolidayDto;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Validated
public interface IDayCounterService {

    int countWeekendsBetweenDates(LocalDate begin, LocalDate end);

    int countHolidaysBetweenDates(LocalDate begin, LocalDate end);

    int getAllDaysQuantityBetweenDates(LocalDate begin, LocalDate end);

    int getWorkingDaysQuantityBetweenDates(LocalDate begin, LocalDate end);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    String createHoliday(@NotNull LocalDate date, @NotBlank String description);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    void editHoliday(@NotNull String id, @NotNull LocalDate date, @NotBlank String description);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    void deleteHoliday(@NotNull String id);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    String copyHolidayToNextYear(@NotNull String id);

    List<HolidayDto> findHolidaysOfYear(@Min(value=0) @Max(value = 3000) int year);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    void copyYearHolidaysToNext(@Min(value=0) @Max(value = 3000) int year);
}
