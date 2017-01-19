package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ViewNames;
import erp.dto.ReportDto;
import erp.dto.UserDto;
import erp.service.IReportService;
import erp.utils.DateParser;
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
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Inject
    private IReportService reportService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getUserReportsPage(@AuthenticationPrincipal UserDto currentUser,
                                     Map<String, Object> model, @RequestParam Optional<String> filter) {

        putReportsModel( currentUser, model, filter );
        return ViewNames.REPORTS.reports;
    }

    @RequestMapping(value = "/userReports", method = RequestMethod.GET)
    public String getUserReportsList(@AuthenticationPrincipal UserDto currentUser,
                                     Map<String, Object> model, @RequestParam Optional<String> filter) {

        putReportsModel( currentUser, model, filter );
        return ViewNames.REPORTS.reportsComponent;
    }

    private void putReportsModel(UserDto currentUser,Map<String, Object> model, Optional<String> filter) {

        if (filter.isPresent()) {
            LocalDate localDate = null;
            LocalDate begin = null;
            LocalDate end = null;
            String filterDate = filter.get();

            if (filterDate.length() == 7) {
                localDate = DateParser.parseMonthDate(filterDate);
                begin = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
                end = LocalDate.of(localDate.getYear(), localDate.getMonth(), begin.lengthOfMonth());
            }
            else if (filterDate.length() == 10) {
                localDate = DateParser.parseDate(filterDate);
                begin = localDate;
                end = localDate;
            }

            model.put(
                    AttributeNames.UserViewReports.userReports,
                    this.reportService.viewUserReportsBetweenDates(currentUser.getId(), begin, end));
        }
        else
            model.put(
                    AttributeNames.UserViewReports.userReports,
                    this.reportService.viewUserReportsBetweenDates(currentUser.getId(), LocalDate.now(), LocalDate.now())
            );
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
}
