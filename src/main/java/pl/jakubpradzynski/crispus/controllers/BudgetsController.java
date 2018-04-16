package pl.jakubpradzynski.crispus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;

import javax.servlet.http.HttpSession;

@Controller
public class BudgetsController {

    @RequestMapping(value = "/budgets", method = RequestMethod.GET)
    public String showBudgets(HttpSession httpSession) throws SessionExpiredException {
        if (httpSession.getAttribute("username") == null) {
            throw new SessionExpiredException("Sesja wygasła!");
        }
        return "budgets.html";
    }

}
