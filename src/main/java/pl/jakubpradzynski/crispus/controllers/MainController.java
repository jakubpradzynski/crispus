package pl.jakubpradzynski.crispus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showMainPage() {
        return "index.html";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String showContactPage() {
        return "contact.html";
    }

    @RequestMapping(value = "/aboutProject", method = RequestMethod.GET)
    public String showAboutProjectPage() {
        return "aboutProject.html";
    }

}
