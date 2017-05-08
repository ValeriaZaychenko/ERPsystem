package erp.restcontroller;

import erp.dto.UserDto;
import erp.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/rest/users")
public class UserRestController {

    @Inject
    private IUserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public List<UserDto> findAll() {
        return userService.viewUsers();
    }

    @RequestMapping(method = RequestMethod.POST)
    public UserDto add(@RequestBody UserDto user) {
        String id = userService.createUser(user.getName(), user.getEmail(), user.getUserRole());
        //TODO a service's create method returning whole UserDto instead of User id
        return userService.findUserById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserDto findOne(@PathVariable String id) {
        return userService.findUserById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public UserDto update(@PathVariable String id, @RequestBody UserDto user) {
        userService.editUser(id, user.getName(), user.getEmail(), user.getUserRole());
        return userService.findUserById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void remove(@PathVariable String id) {
        userService.removeUser(id);
    }
}
