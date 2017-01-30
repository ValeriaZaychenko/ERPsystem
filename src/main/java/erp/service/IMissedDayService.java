package erp.service;

import erp.dto.MissedDayDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Validated
public interface IMissedDayService {

    String createMissedDay(
            @NotNull LocalDate date,
            @NotNull String missedDayType,
            @NotNull String userId);

    void editMissedDay(
            @NotNull String id,
            @NotNull LocalDate date,
            @NotNull String missedDayType,
            @NotNull String userId);

    void deleteMissedDay(@NotNull String id);

    List<MissedDayDto> viewUserMissedDaysBetweenDates(
            @NotNull LocalDate begin,
            @NotNull LocalDate end,
            @NotNull String userId);

    void markDayOffAsVacation(@NotNull String id);
}
