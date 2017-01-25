package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ViewNames;
import erp.dto.ReportDto;
import erp.dto.UserDto;
import erp.exceptions.ApplyGroupByException;
import erp.exceptions.UnknownGroupByException;
import erp.service.IDayCounterService;
import erp.service.IReportService;
import erp.utils.DateParser;
import erp.utils.GroupBy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final int DATE_LENGTH_MONTH_FORMAT = 7; //E.g "YYYY-MM"
    private final int DATE_LENGTH_FULL_FORMAT = 10; //E.g "YYYY-MM-DD"

    @Inject
    private IReportService reportService;
    @Inject
    private IDayCounterService dayCounterService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getUserReportsPage(@AuthenticationPrincipal UserDto currentUser,
                                     Map<String, Object> model,
                                     @RequestParam Optional<String> filter,
                                     @RequestParam Optional<String> groupBy) {

        checkFilterAndGrouping(currentUser, model, filter, groupBy);
        return ViewNames.REPORTS.reports;
    }

    @RequestMapping(value = "/userReports", method = RequestMethod.GET)
    public String getUserReportsList(@AuthenticationPrincipal UserDto currentUser,
                                     Map<String, Object> model,
                                     @RequestParam Optional<String> filter,
                                     @RequestParam Optional<String> groupBy) {

        checkFilterAndGrouping(currentUser, model, filter, groupBy);
        return ViewNames.REPORTS.reportsComponent;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity add(@AuthenticationPrincipal UserDto currentUser,
                              @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              double duration, String description, boolean remote) {

        reportService.createReport(date, duration, description, currentUser.getId(), remote);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseEntity edit(@RequestParam String reportId,
                             @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             @RequestParam double duration, @RequestParam String description, boolean remote) {
        reportService.editReport(reportId, date, duration, description, remote);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity delete(@RequestParam String reportId) {
        reportService.removeReport(reportId);
        return new ResponseEntity(HttpStatus.OK);
    }

    /*
    Apply filter, grouping and put to model
    Default filter: today
     */
    private void checkFilterAndGrouping(UserDto currentUser, Map<String, Object> model,
                                        Optional<String> filter, Optional<String> groupBy) {

        LocalDate begin = null;
        LocalDate end = null;

        if (filter.isPresent()) {
            LocalDate localDate = null;
            String filterDate = filter.get();

            //Check if filter string came in month format or full format
            if (filterDate.length() == DATE_LENGTH_MONTH_FORMAT) {
                localDate = DateParser.parseMonthDate(filterDate);
                begin = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
                end = LocalDate.of(localDate.getYear(), localDate.getMonth(), begin.lengthOfMonth());
            }
            else if (filterDate.length() == DATE_LENGTH_FULL_FORMAT) {
                localDate = DateParser.parseDate(filterDate);
                begin = localDate;
                end = localDate;
            }
        }
        else
            begin = end = LocalDate.now();

        applyGroupByAndPutToModel(
                model, groupBy,
                this.reportService.viewUserReportsBetweenDates(currentUser.getId(), begin, end));
        model.put(
                AttributeNames.UserViewReports.userProgress,
                reportService.getUserWorkingTimeBetweenDates(currentUser.getId(), begin, end)
                        .getProgress());
        model.put(
                AttributeNames.UserViewReports.sumOfDurations,
                reportService.getUserWorkingTimeBetweenDates(currentUser.getId(), begin, end)
                        .getUserCurrentMonthWorkingTime());
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
    }

    /*
    Apply grouping and put to model
    Throws UnknownGroupByException, ApplyGroupByException
     */
    private void applyGroupByAndPutToModel(Map<String, Object> model, Optional<String> groupBy, List<ReportDto> dtos) {

        if (! groupBy.isPresent()) {
            model.put(
                    AttributeNames.UserViewReports.userReports,
                    dtos);
            return;
        }

        GroupBy g = getOrderEnumFromString(groupBy.get());

        List<ReportDto> sortedDtos;

        switch (g) {

            case DATE_DIRECT_ORDER:
                sortedDtos = dtos.stream()
                        .sorted((p1, p2) -> p1.getDate().compareTo(p2.getDate()))
                        .collect(Collectors.toList());
                break;

            case DATE_REVERSE_ORDER:
                sortedDtos = dtos.stream()
                        .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                        .collect(Collectors.toList());
                break;

            case DURATION_DIRECT_ORDER:
                sortedDtos = dtos.stream()
                        .sorted((p1, p2) -> Double.compare(p1.getDuration(), p2.getDuration()))
                        .collect(Collectors.toList());
                break;

            case DURATION_REVERSE_ORDER:
                sortedDtos = dtos.stream()
                        .sorted((p1, p2) -> Double.compare(p2.getDuration(), p1.getDuration()))
                        .collect(Collectors.toList());
                break;

            case DESCRIPTION_DIRECT_ORDER:
                sortedDtos = dtos.stream()
                        .sorted((p1, p2) -> p1.getDescription().compareTo(p2.getDescription()))
                        .collect(Collectors.toList());
                break;

            case DESCRIPTION_REVERSE_ORDER:
                sortedDtos = dtos.stream()
                        .sorted((p1, p2) -> p2.getDescription().compareTo(p1.getDescription()))
                        .collect(Collectors.toList());
                break;

            case REMOTE_FIRST:
                sortedDtos = dtos.stream()
                        .sorted( (p1, p2) -> Boolean.compare( p2.isRemote(), p1.isRemote() ) )
                        .collect(Collectors.toList());
                break;

            case REMOTE_LAST:
                sortedDtos = dtos.stream()
                        .sorted( (p1, p2) -> Boolean.compare( p1.isRemote(), p2.isRemote() ) )
                        .collect(Collectors.toList());
                break;

            default:
                throw new ApplyGroupByException();
        }

        model.put(
                AttributeNames.UserViewReports.userReports,
                sortedDtos);
    }

    private GroupBy getOrderEnumFromString(String groupBy) {
        try {
            GroupBy g = GroupBy.valueOf(groupBy);
            return g;
        } catch (Exception e) {
            throw new UnknownGroupByException(groupBy);
        }
    }

}
