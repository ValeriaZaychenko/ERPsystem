package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ErrorKeys;
import erp.controller.constants.ViewNames;
import erp.dto.UserDto;
import erp.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.util.Map;

@Controller
public class IndexController {

    @Inject
    private IUserService userService;

    @RequestMapping("/")
    public View index() {
        return new RedirectView("/home");
    }

    @RequestMapping("/accessDenied")
    public String accessDeniedError(Map<String, Object> model) {
        model.put(AttributeNames.ErrorView.message, ErrorKeys.AccessDeniedMessage);
        model.put(AttributeNames.ErrorView.attribute, "");
        return ViewNames.ERROR.error;
    }

    @RequestMapping("/home")
    public View home(@AuthenticationPrincipal UserDto currentUser) {
        if (currentUser != null && currentUser.getUserRole().equals("ADMIN"))
            return new RedirectView("/users");
        else {
            if (userService.needToChangePassword(currentUser))
                return new RedirectView("/changePassword/");
            else
                return new RedirectView("/reports");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return ViewNames.LOGIN.login;
    }

    @RequestMapping(path = "/setlocale/", method = RequestMethod.POST)
    public ResponseEntity setLocale(HttpSession session, @RequestParam String language) {
        Locale locale = null;
        if (language.equalsIgnoreCase("ru"))
            locale = new Locale("ru", "RU");

        else
            locale = new Locale("en");

        session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/changePassword/", method = RequestMethod.GET)
    public String changePassword() {
        return ViewNames.SETTINGS.settings;
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
