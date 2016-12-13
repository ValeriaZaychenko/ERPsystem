package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.SessionKeys;
import erp.controller.constants.ViewNames;
import erp.domain.User;
import erp.dto.UserDto;
import erp.service.IReportService;
import erp.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Inject
    private IReportService reportService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getUserReportsList(HttpSession session, Map<String, Object> model) {
        UserDto userDto = (UserDto) session.getAttribute(SessionKeys.USER.user);

        model.put(AttributeNames.UserViewReports.userReports, this.reportService.viewUserReports(userDto.getId()));

        return ViewNames.REPORTS.reports;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RedirectView add(HttpSession session, String date, int time, String description) {
        UserDto userDto = (UserDto) session.getAttribute(SessionKeys.USER.user);

        reportService.createReport(date, time, description, userDto.getId());
        return new RedirectView("/reports");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RedirectView edit(@RequestParam String reportId, @RequestParam String date,
                             @RequestParam int time, @RequestParam String description) {
        reportService.editReport(reportId, date, time, description);
        return new RedirectView("/reports");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RedirectView delete(@RequestParam String reportId) {
        reportService.removeReport(reportId);
        return new RedirectView("/reports");
    }
}
