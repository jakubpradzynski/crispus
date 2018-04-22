package pl.jakubpradzynski.crispus.utils;

import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;

import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static void isUserSessionActive(HttpSession httpSession) throws SessionExpiredException {
        if (httpSession.getAttribute("username") == null) {
            throw new SessionExpiredException("Sesja wygasła!");
        }
    }
}
