package erp.service;

import erp.dto.HolidayDto;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Validated
public interface IDayCounterService {

    int countWeekendsBetweenDates(
            LocalDate begin,
            LocalDate end);

    int getAllDaysQuantityBetweenDates(
            LocalDate begin,
            LocalDate end);

    String createHoliday(@NotNull LocalDate date, @NotBlank String description);

    void editHoliday(@NotNull String id, @NotNull LocalDate date, @NotBlank String description);

    void deleteHoliday(@NotNull String id);

    String copyHolidayToNextYear(@NotNull String id);

    List<HolidayDto> findHolidaysOneYear(@Min(value=0) @Max(value = 3000) int year);

    void copyYearHolidaysToNext(@Min(value=0) @Max(value = 3000) int year);
}
