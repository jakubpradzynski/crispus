package pl.jakubpradzynski.crispus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * A controller-type class for handling main requests.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Controller
public class MainController {

    /**
     * Method supports request GET for a path "/".
     * Shows the start page.
     * @return String (index.html)
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showMainPage() {
        return "index.html";
    }

    /**
     * Method supports request GET for a path "/contact".
     * Shows the contact page.
     * @return String (contact.html)
     */
    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String showContactPage() {
        return "contact.html";
    }

    /**
     * Method supports request GET for a path "/aboutProject".
     * Shows the about project page.
     * @return String (aboutProject.html)
     */
    @RequestMapping(value = "/aboutProject", method = RequestMethod.GET)
    public String showAboutProjectPage() {
        return "aboutProject.html";
    }

}
