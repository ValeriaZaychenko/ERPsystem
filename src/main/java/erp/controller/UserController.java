package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.domain.UserRole;
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    String getUsersList(Map<String, Object> model) {
        model.put(AttributeNames.UserView.Users, this.userService.viewUsers());
        model.put(AttributeNames.UserView.PossibleUserRoles, UserRole.values());
        return "user";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity add(String name, String email, String userRole) {
        userService.createUser(name, email, userRole);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseEntity edit(String id, String name, String email, String userRole) {
        userService.changeAllFields(id, name, email, userRole);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity delete(String id) {
        userService.removeUser(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
