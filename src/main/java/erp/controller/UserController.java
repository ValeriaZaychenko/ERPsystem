package erp.controller;

import erp.domain.User;
import erp.service.IUserService;
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
        return "user";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(String id) {
        userService.removeUser(id);
        return "redirect:/users";
    }
}
