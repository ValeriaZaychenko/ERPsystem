package erp.restcontroller;

import erp.dto.ProgressDto;
import erp.service.IProgressService;
import erp.utils.DateParser;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/rest/progress")
public class ProgressRestController {

    @Inject
    private IProgressService progressService;

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ProgressDto getProgress(@PathVariable Optional<String> month, @PathVariable String userId) {

        if (month.isPresent()) {
            LocalDate localDate = DateParser.parseMonthDate(month.get());

            LocalDate begin = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
            LocalDate end = LocalDate.of(localDate.getYear(), localDate.getMonth(), begin.lengthOfMonth());

            return progressService.getUserProgressBetweenDates(userId, begin, end);
        }
        else
            return progressService.getUserProgressBetweenDates(
                    userId, getCurrentMonthBeginDate(), getCurrentMonthEndDate());
    }

    private LocalDate getCurrentMonthBeginDate() {
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        return LocalDate.of(year, month, 1);
    }

    private LocalDate getCurrentMonthEndDate() {
        int day = LocalDate.now().lengthOfMonth();
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        return LocalDate.of(year, month, day);
    }
}
