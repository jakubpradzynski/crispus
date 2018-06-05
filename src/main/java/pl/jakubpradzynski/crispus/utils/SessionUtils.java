package pl.jakubpradzynski.crispus.utils;

import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;

import javax.servlet.http.HttpSession;

/**
 * A utils-type class with helpful methods related to session.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
public class SessionUtils {

    /**
     * Method checks whether the session has not expired, i.e. whether there is a saved username attribute.
     * @param httpSession - HttpSession
     * @return boolean (true when session is still active)
     * @throws SessionExpiredException - Exception thrown when session has expired.
     */
    public static boolean isUserSessionActive(HttpSession httpSession) throws SessionExpiredException {
        if (httpSession.getAttribute("username") == null) {
            throw new SessionExpiredException("Sesja wygasła!");
        }
        return true;
    }
}
