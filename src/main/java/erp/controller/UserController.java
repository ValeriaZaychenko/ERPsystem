package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ViewNames;
import erp.domain.UserRole;
import erp.service.IReportService;
import erp.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.Map;

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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity add(String name, String email, String userRole) {
        userService.createUser(name, email, userRole);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseEntity edit(String id, String name, String email, String userRole) {
        userService.editUser(id, name, email, userRole);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity delete(String id) {
        userService.removeUser(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/progress", method = RequestMethod.GET)
    public String getUsersProgressList(Map<String, Object> model) {
        model.put(AttributeNames.UserViewUsers.progress, reportService.getAllUsersCurrentMonthWorkingTime());
        return ViewNames.PROGRESS.progress;
    }
}
