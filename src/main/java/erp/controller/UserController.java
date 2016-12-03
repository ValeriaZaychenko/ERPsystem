package erp.controller;

import erp.domain.User;
import erp.domain.UserRole;
import erp.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
    String getEmployeesList(Map<String, Object> model) {
        model.put("users", this.userService.viewUsers());
        //model.put("possibleUserRoles", UserRole.values());
        return "user";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity add(String name, String email) {
        userService.createUser(name, email, "USER");
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseEntity add(String id, String name, String email) {
        userService.changeUserName(id, name);
        userService.changeUserEmail(id, email);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity delete(String id) {
        userService.removeUser(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
