package pl.jakubpradzynski.crispus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.jakubpradzynski.crispus.dto.UserDto;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.exceptions.EmailExistsException;
import pl.jakubpradzynski.crispus.services.UserService;

import javax.validation.Valid;

/**
 * A controller-type class for handling registration-related requests.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    /**
     * Method supports request GET for a path "/registration".
     * Shows registration form page.
     * @param model - Model from MVC
     * @return String (registration.html)
     */
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    /**
     * Method supports request POST for a path "/registration".
     * Validates data from the user and, depending on the result, register new user or returns an error.
     * @param accountDto - user data
     * @param result - BindingResult
     * @return Model and View (successRegister.html when registration was successful and registration.html otherwise)
     */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registerUserAccount
            (@ModelAttribute("user") @Valid UserDto accountDto,
             BindingResult result) {
        User registered = null;
        if (!result.hasErrors()) {
            registered = createUserAccount(accountDto);
        }
        if (registered == null) {
            result.rejectValue("email", "Niepoprawny email!");
        }
        if (result.hasErrors()) {
            return new ModelAndView("registration", "user", accountDto);
        }
        else {
            return new ModelAndView("successRegister");
        }
    }

    /**
     * Method register new user.
     * @param accountDto - user data
     * @return User when registration was successful and null otherwise
     */
    private User createUserAccount(UserDto accountDto) {
        User registered;
        try {
            registered = userService.registerNewUserAccount(accountDto);
        } catch (EmailExistsException e) {
            return null;
        }
        return registered;
    }

}
