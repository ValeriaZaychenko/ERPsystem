package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ViewNames;
import erp.dto.UserDto;
import erp.service.IReportService;
import erp.utils.DateParser;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Inject
    private IReportService reportService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getUserReportsList(@AuthenticationPrincipal UserDto currentUser,
                                     Map<String, Object> model) {
        model.put(
                AttributeNames.UserViewReports.userReports,
                this.reportService.viewUserReports(currentUser.getId())
        );

        return ViewNames.REPORTS.reports;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RedirectView add(@AuthenticationPrincipal UserDto currentUser,
                            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                            int time, String description, String remote) {

        reportService.createReport(date, time, description, currentUser.getId(), remote);
        return new RedirectView("/reports");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RedirectView edit(@RequestParam String reportId,
                             @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             @RequestParam int time, @RequestParam String description, String remote) {
        reportService.editReport(reportId, date, time, description, remote);
        return new RedirectView("/reports");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RedirectView delete(@RequestParam String reportId) {
        reportService.removeReport(reportId);
        return new RedirectView("/reports");
    }
}
