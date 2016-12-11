package erp.controller.constants;

import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public interface SessionKeys {

    interface LOCALE {
        String locale = SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME;
    }

    interface USER {
        String user = "USER";
    }
}
