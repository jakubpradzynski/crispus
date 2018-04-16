package pl.jakubpradzynski.crispus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;

import javax.servlet.http.HttpSession;

@Controller
public class AccountsController {

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public String showAccounts(HttpSession httpSession) throws SessionExpiredException {
        if (httpSession.getAttribute("username") == null) {
            throw new SessionExpiredException("Sesja wygasła!");
        }
        return "accounts.html";
    }
}
