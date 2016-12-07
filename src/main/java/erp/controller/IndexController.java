package erp.controller;

import erp.controller.constants.SessionKeys;
import erp.controller.constants.ViewNames;
import erp.dto.UserDto;
import erp.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
public class IndexController {

    @Inject
    private IUserService userService;

    @RequestMapping("/")
    public String index () {
        return ViewNames.HOME;
    }

    @RequestMapping(path = "/setlocale/", method = RequestMethod.POST)
    public ResponseEntity setLocale (HttpSession session, @RequestParam String language) {
        Locale locale = null;
        if (language.equalsIgnoreCase("ru"))
            locale = new Locale("ru", "RU");

        else if (language.equalsIgnoreCase("uk"))
            locale = new Locale("uk", "UA");

        else
            locale = new Locale("en");

        session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "/login/", method = RequestMethod.POST)
    public View login(HttpSession session, @RequestParam String userLogin, @RequestParam String password) {
        UserDto userDto = userService.authenticate(userLogin, password);

        if(userDto != null) {
            session.setAttribute(SessionKeys.USER, userDto);
            return new RedirectView("/users/");
        }
        return new RedirectView("/");
    }

    @RequestMapping(path = "/logout/", method = RequestMethod.POST)
    public View logout(HttpSession session) {
        session.removeAttribute(SessionKeys.USER);
        return new RedirectView("/");
    }

    @RequestMapping(value = "/changePassword/", method = RequestMethod.GET)
    public String changePassword() {
        return ViewNames.SETTINGS;
    }

    /*
    Get userId from jsp session attr
    May be discussed
     */
    @RequestMapping(value = "/changePassword/", method = RequestMethod.POST)
    public View changePassword(
            @RequestParam String userId, @RequestParam String oldPassword, @RequestParam String newPassword) {

        userService.changePassword(userId, oldPassword, newPassword);
        return new RedirectView("/");
    }
}
