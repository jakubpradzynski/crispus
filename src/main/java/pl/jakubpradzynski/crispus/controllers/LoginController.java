package pl.jakubpradzynski.crispus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.jakubpradzynski.crispus.dto.UserLoginDto;
import pl.jakubpradzynski.crispus.services.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * A controller-type class for handling login-related requests.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession httpSession;

    /**
     * Method supports request GET for a path "/login".
     * The method checks if the user is already logged in.
     * If not, it moves to the login form, if so to the home page.
     * @param model - Model from MVC
     * @return String (login.html when user didn't log in or homepage.html)
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginForm(Model model) {
        if (httpSession.getAttribute("username") != null) {
            return "redirect:/homepage";
        }
        UserLoginDto userLoginDto = new UserLoginDto();
        model.addAttribute("user", userLoginDto);
        return "login";
    }

    /**
     * Method supports request POST for a path "/login".
     * Checks the validity of login data, in the case of success it saves the username in the session attribute, otherwise it returns an error.
     * @param userLoginDto - user login data
     * @param result - BindingResult
     * @param httpSession - HttpSession
     * @return - Model and View (homepage.html after successful login or login.html if error occurred)
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginIn
            (@ModelAttribute("user") @Valid UserLoginDto userLoginDto,
             BindingResult result, HttpSession httpSession) {
        if (result.hasErrors()) {
            return new ModelAndView("/login", "user", userLoginDto);
        }
        if (userService.loginCheck(userLoginDto)) {
            httpSession.setAttribute("username", userLoginDto.getLogin());
            return new ModelAndView("redirect:/homepage", "user", userLoginDto);
        } else {
            result.rejectValue("password", "Niepoprawny login lub hasło!");
        }
        return new ModelAndView("/login", "user", userLoginDto);
    }

    /**
     * Method supports request GET for a path "/logout".
     * Remove the username from session attribute.
     * @param httpSession - HttpSession
     * @return String (successLogout.html)
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("username");
        return "successLogout";
    }

}
