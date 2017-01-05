package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ViewNames;
import erp.service.IDayCounterService;
import erp.service.IReportService;
import erp.utils.DateParser;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Controller
public class ProgressController {

    @Inject
    private IReportService reportService;
    @Inject
    private IDayCounterService dayCounterService;

    @RequestMapping(value = "/progress", method = RequestMethod.GET)
    public String getUsersProgressList(Map<String, Object> model,
                                       @RequestParam Optional<String> month) {

        if (month.isPresent()) {
            LocalDate localDate = DateParser.parseMonthDate(month.get());

            LocalDate begin = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
            LocalDate end = LocalDate.of(localDate.getYear(), localDate.getMonth(), begin.lengthOfMonth());

            putAttrToModel(model, begin, end);
        }
        else
            putAttrToModel(model, getCurrentMonthBeginDate(), getCurrentMonthEndDate());

        return ViewNames.PROGRESS.progress;
    }

    @RequestMapping(value = "/holidays", method = RequestMethod.GET)
    public String getHolidaysOfYear(Map<String, Object> model) {
        model.put(
                AttributeNames.ProgressView.holiday,
                dayCounterService.findHolidaysOfYear(LocalDate.now().getYear()));

        return ViewNames.HOLIDAY.holiday;
    }

    @RequestMapping(value = "/holidays/add", method = RequestMethod.POST)
    public RedirectView addHoliday(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            String description) {

        dayCounterService.createHoliday(date, description);
        return new RedirectView("/holidays");
    }

    private void putAttrToModel(Map<String, Object> model, LocalDate begin, LocalDate end) {
        model.put(
                AttributeNames.ProgressView.progress,
                reportService.getAllUsersWorkingTimeBetweenDates(begin, end));
        model.put(
                AttributeNames.ProgressView.weekends,
                dayCounterService.countWeekendsBetweenDates(begin, end));
        model.put(
                AttributeNames.ProgressView.holiday,
                dayCounterService.countHolidaysBetweenDates(begin, end));
        model.put(
                AttributeNames.ProgressView.allDays,
                dayCounterService.getAllDaysQuantityBetweenDates(begin, end));
        model.put(
                AttributeNames.ProgressView.monthName,
                begin.getMonth().toString());
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
