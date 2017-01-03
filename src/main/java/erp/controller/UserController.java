package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ViewNames;
import erp.domain.UserRole;
import erp.exceptions.DuplicateEmailException;
import erp.service.IReportService;
import erp.service.IUserService;
import erp.utils.DateParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Controller
@RequestMapping("/users")
public class UserController {

    @Inject
    private IUserService userService;
    @Inject
    private IReportService reportService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getUsersList(Map<String, Object> model) {
        model.put(AttributeNames.UserViewUsers.users, this.userService.viewUsers());
        model.put(AttributeNames.UserViewPossibleUserRoles.possibleUserRoles, UserRole.values());
        return ViewNames.USER.user;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST )
    public Object add(
            HttpServletResponse response,
            Map<String, Object> model,
            String name,
            String email,
            String userRole
    ) {
        return runWithCheckDuplicateEmail(
            response, model,
            () -> userService.createUser(name, email, userRole)
        );
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Object edit(
            HttpServletResponse response,
            Map<String, Object> model,
            String id,
            String name,
            String email,
            String userRole
    )  {
        return runWithCheckDuplicateEmail(
            response, model,
            () -> { userService.editUser(id, name, email, userRole); return null; }
        );
    }

    private Object runWithCheckDuplicateEmail (HttpServletResponse response,
                                                Map<String, Object> model,
                                                Supplier<Object> supplier) {
        try {
            supplier.get();
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (DuplicateEmailException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST );
            model.put(AttributeNames.ErrorPlainView.errorMessage, e.getName());
            return ViewNames.ERROR.errorPlain;
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity delete(String id) {
        userService.removeUser(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/progress", method = RequestMethod.GET)
    public String getUsersProgressList(Map<String, Object> model,
                                       @RequestParam Optional<String> month) {

        if (month.isPresent()) {
            LocalDate localDate = DateParser.parseMonthDate(month.get());

            LocalDate begin = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
            LocalDate end = LocalDate.of(localDate.getYear(), localDate.getMonth(), begin.lengthOfMonth());

            model.put(
                    AttributeNames.UserViewUsers.progress,
                    reportService.getAllUsersWorkingTimeBetweenDates(begin, end));
        }
        else
            model.put(
                    AttributeNames.UserViewUsers.progress,
                    reportService.getAllUsersWorkingTimeBetweenDates(
                            getCurrentMonthBeginDate(), getCurrentMonthEndDate()));

        return ViewNames.PROGRESS.progress;
    }

    private LocalDate getCurrentMonthBeginDate() {
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        return LocalDate.of(year, month, 1);
    }

    private LocalDate getCurrentMonthEndDate() {
        int day = LocalDate.now().getDayOfMonth();
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        return LocalDate.of(year, month, day);
    }
}
