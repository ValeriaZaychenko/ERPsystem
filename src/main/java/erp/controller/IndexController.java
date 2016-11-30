package erp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
public class IndexController {

    @RequestMapping("/")
    public View index () {
        return new RedirectView("/users/", true, false);
    }

    @RequestMapping( path = "/setlocale/", method = RequestMethod.POST )
    public ResponseEntity setLocale (HttpSession session, @RequestParam String language ) {
        Locale locale = null;
        if ( language.equalsIgnoreCase( "ru" ) )
            locale = new Locale( "ru", "RU" );

        else if ( language.equalsIgnoreCase( "uk" ) )
            locale = new Locale( "uk", "UA" );

        else
            locale = new Locale( "en" );

        session.setAttribute( SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale );
        return new ResponseEntity( HttpStatus.OK );
    }
}
