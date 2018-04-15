package pl.jakubpradzynski.crispus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showMainPage(WebRequest request, Model model) {
        return "index.html";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String showContactPage(WebRequest request, Model model) {
        return "contact.html";
    }

    @RequestMapping(value = "/aboutProject", method = RequestMethod.GET)
    public String showAboutProjectPage(WebRequest request, Model model) {
        return "aboutProject.html";
    }

}
