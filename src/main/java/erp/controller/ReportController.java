package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.SessionKeys;
import erp.controller.constants.ViewNames;
import erp.domain.User;
import erp.dto.UserDto;
import erp.service.IReportService;
import erp.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}
