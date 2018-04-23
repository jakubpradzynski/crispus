package pl.jakubpradzynski.crispus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.jakubpradzynski.crispus.dto.UserLoginDto;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;
import pl.jakubpradzynski.crispus.services.UserService;
import pl.jakubpradzynski.crispus.utils.SessionUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession httpSession;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginForm(WebRequest request, Model model) {
        if (httpSession.getAttribute("username") != null) {
            return "redirect:/homepage";
        }
        UserLoginDto userLoginDto = new UserLoginDto();
        model.addAttribute("user", userLoginDto);
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginIn
            (@ModelAttribute("user") @Valid UserLoginDto userLoginDto,
             BindingResult result, WebRequest request, Errors errors, HttpSession httpSession) {
        if (result.hasErrors()) {
            return new ModelAndView("/login", "user", userLoginDto);
        }
        if (userService.loginCheck(userLoginDto)) {
            httpSession.setAttribute("username", userLoginDto.getLogin());
            return new ModelAndView("redirect:/homepage", "user", userLoginDto);
        } else {
            result.rejectValue("password", "invalid.login.or.password");
        }
        return new ModelAndView("/login", "user", userLoginDto);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(WebRequest request, Model model, HttpSession httpSession) {
        httpSession.removeAttribute("username");
        return "succesLogout";
    }

}
