package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ViewNames;
import erp.domain.UserRole;
import erp.exceptions.DuplicateEmailException;
import erp.service.IReportService;
import erp.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
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
}
