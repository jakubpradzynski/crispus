package pl.jakubpradzynski.crispus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;
import pl.jakubpradzynski.crispus.services.UserTypeService;
import pl.jakubpradzynski.crispus.utils.SessionUtils;

import javax.servlet.http.HttpSession;

/**
 * A controller-type class for handling user-type-related requests.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Controller
public class UserTypeController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private UserTypeService userTypeService;

    /**
     * Method supports request GET for a path "/premium".
     * Change user type to premium.
     * @return String (premium.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/premium", method = RequestMethod.GET)
    public String premium() throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        userTypeService.changeUserType((String) httpSession.getAttribute("username"), "premium");
        return "premium";
    }

}
