package pl.jakubpradzynski.crispus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;

import javax.servlet.http.HttpSession;

@Controller
public class TransactionsController {

    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public String showTransactions(HttpSession httpSession) throws SessionExpiredException {
        if (httpSession.getAttribute("username") == null) {
            throw new SessionExpiredException("Sesja wygasła!");
        }
        return "transactions.html";
    }

}
