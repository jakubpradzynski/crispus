package pl.jakubpradzynski.crispus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;
import pl.jakubpradzynski.crispus.services.UserTypeService;
import pl.jakubpradzynski.crispus.utils.SessionUtils;

import javax.servlet.http.HttpSession;

@Controller
public class UserTypeController {

    @Autowired
    HttpSession httpSession;

    @Autowired
    UserTypeService userTypeService;

    @RequestMapping(value = "/premium", method = RequestMethod.GET)
    public String premium() throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        userTypeService.changeUserType((String) httpSession.getAttribute("username"), "premium");
        return "premium";
    }

}
