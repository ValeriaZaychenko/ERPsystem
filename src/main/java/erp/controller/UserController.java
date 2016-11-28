package erp.controller;

import erp.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class UserController {

    @Inject
    private IUserService userService;

    @RequestMapping(path = {"/users"}, method = RequestMethod.GET)
    String getEmployeesList(Map<String, Object> model) {
        model.put("users", this.userService.viewUsers());
        return "user";
    }
}
