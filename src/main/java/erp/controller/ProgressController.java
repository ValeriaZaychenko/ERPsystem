package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ViewNames;
import erp.service.IDayCounterService;
import erp.service.IProgressService;
import erp.utils.DateParser;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Controller
public class ProgressController {

    @Inject
    private IProgressService progressService;
    @Inject
    private IDayCounterService dayCounterService;

    @RequestMapping(value = "/progress", method = RequestMethod.GET)
    public String getUsersProgressList(Map<String, Object> model,
                                       @RequestParam Optional<String> month) {

        if (month.isPresent()) {
            LocalDate localDate = DateParser.parseMonthDate(month.get());

            LocalDate begin = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
            LocalDate end = LocalDate.of(localDate.getYear(), localDate.getMonth(), begin.lengthOfMonth());

            putProgressAttrToModel(model, begin, end);
        }
        else
            putProgressAttrToModel(model, getCurrentMonthBeginDate(), getCurrentMonthEndDate());

        return ViewNames.PROGRESS.progress;
    }

    @RequestMapping(value = "/holidays", method = RequestMethod.GET)
    public String getHolidaysOfYear(Map<String, Object> model, @RequestParam Optional<String> year) {

        if (year.isPresent())
            putHolidaysAttrToModel(model, Integer.valueOf(year.get()));

        else
            putHolidaysAttrToModel(model, LocalDate.now().getYear());

        return ViewNames.HOLIDAY.holiday;
    }

    @RequestMapping(value = "/holidays/add", method = RequestMethod.POST)
    public ResponseEntity addHoliday(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            String description) {

        dayCounterService.createHoliday(date, description);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/holidays/edit", method = RequestMethod.POST)
    public ResponseEntity edit(@RequestParam String holidayId,
                               @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                               @RequestParam String description) {
        dayCounterService.editHoliday(holidayId, date, description);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "holidays/delete", method = RequestMethod.POST)
    public ResponseEntity delete(@RequestParam String holidayId) {
        dayCounterService.deleteHoliday(holidayId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "holidays/holiday/clone", method = RequestMethod.POST)
    public ResponseEntity cloneOne(@RequestParam String holidayId) {
        dayCounterService.copyHolidayToNextYear(holidayId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "holidays/clone", method = RequestMethod.POST)
    public ResponseEntity cloneAll(@RequestParam int year) {
        dayCounterService.copyYearHolidaysToNext(year);
        return new ResponseEntity(HttpStatus.OK);
    }

    private void putProgressAttrToModel(Map<String, Object> model, LocalDate begin, LocalDate end) {
        model.put(
                AttributeNames.ProgressView.progress,
                progressService.getAllUsersProgressBetweenDates(begin, end));
        model.put(
                AttributeNames.ProgressView.weekends,
                dayCounterService.countWeekendsBetweenDates(begin, end));
        model.put(
                AttributeNames.ProgressView.holiday,
                dayCounterService.countHolidaysBetweenDates(begin, end));
        model.put(
                AttributeNames.ProgressView.workingDays,
                dayCounterService.getWorkingDaysQuantityBetweenDates(begin, end));
        model.put(
                AttributeNames.ProgressView.allDays,
                dayCounterService.getAllDaysQuantityBetweenDates(begin, end));
        model.put(
                AttributeNames.ProgressView.monthDate,
                begin.getMonth().getValue() + "/" + begin.getYear());
    }

    private void putHolidaysAttrToModel(Map<String, Object> model, int year) {
        model.put(
                AttributeNames.ProgressView.holiday,
                dayCounterService.findHolidaysOfYear(year));
        model.put(
                AttributeNames.ProgressView.holidaysYear,
                year);
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
