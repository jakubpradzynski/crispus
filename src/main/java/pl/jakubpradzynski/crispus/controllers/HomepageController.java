package pl.jakubpradzynski.crispus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;
import pl.jakubpradzynski.crispus.services.DataService;
import pl.jakubpradzynski.crispus.services.TransactionService;
import pl.jakubpradzynski.crispus.utils.SessionUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import static pl.jakubpradzynski.crispus.utils.RequestUtils.isErrorOccurred;

/**
 * A controller-type class for handling homepage requests.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Controller
@PropertySource("classpath:/i18n/messages_pl.properties")
public class HomepageController {

    @Autowired
    private DataService dataService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private Environment environment;

    /**
     * Method supports request GET for a path "/homepage".
     * Adds the necessary data about user to model and show home page.
     * @param model - Model from MVC
     * @return String (homepage.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public String showHomepage(Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addAttributesToModel(model);
        return "homepage.html";
    }

    /**
     * Method supports request POST for a path "/homepage".
     * Validates data from the user and, depending on the result, add new user transaction or returns an error.
     * @param transactionDto - info about new transaction from user
     * @param result - BindingResult
     * @param errors - Errors
     * @param model - Model from MVC
     * @return Model and View (homepage.html)
     * @throws ParseException - Exception is thrown when it is impossible to parse the date from the string.
     */
    @RequestMapping(value = "/homepage", method = RequestMethod.POST)
    public ModelAndView addNewTransaction
            (@ModelAttribute("newTransactionDto") @Valid TransactionDto transactionDto,
             BindingResult result, Errors errors, Model model) throws ParseException {

        if (!result.hasErrors()) {
            transactionService.addNewUserTransaction(transactionDto);
        }

        if (result.hasErrors()) {
            model.addAttribute("newTransactionDto", transactionDto);
            addAttributesToModel(model);
            addErrorsAttributesToModel(errors, model);
            return new ModelAndView("homepage", "model", model);
        }
        return new ModelAndView("redirect:/homepage");
    }

    /**
     * Method add necessary attributes about user to model.
     * @param model - Model from MVC
     */
    private void addAttributesToModel(Model model) {
        List<String> accountNames = dataService.getUserAccountsNames((String) httpSession.getAttribute("username"));
        Collections.sort(accountNames, String.CASE_INSENSITIVE_ORDER);
        List<String> placesNames = dataService.getPlacesNamesAvailableForUser((String) httpSession.getAttribute("username"));
        Collections.sort(placesNames, String.CASE_INSENSITIVE_ORDER);
        List<String> transactionCategoriesNames = dataService.getTransactionCategoriesNamesAvailableForUser((String) httpSession.getAttribute("username"));
        Collections.sort(transactionCategoriesNames, String.CASE_INSENSITIVE_ORDER);
        model.addAttribute("userPublicData", dataService.getPublicUserData((String) httpSession.getAttribute("username")));
        model.addAttribute("lastTenTransactionsDto", dataService.getUserLastTenTransactionsDto((String) httpSession.getAttribute("username")));
        model.addAttribute("newTransactionDto", new TransactionDto((String) httpSession.getAttribute("username")));
        model.addAttribute("userAccountsNames", accountNames);
        model.addAttribute("placesNames", placesNames);
        model.addAttribute("transactionCategoriesNames", transactionCategoriesNames);
    }

    /**
     * Method add specific occurred errors to model.
     * @param errors - Errors
     * @param model - Model from MVC
     */
    private void addErrorsAttributesToModel(Errors errors, Model model) {
        if (isErrorOccurred(errors, "value")) model.addAttribute("invalidValue", environment.getProperty("Niepoprawna kwota transakcji!"));
        if (isErrorOccurred(errors, "description")) model.addAttribute("invalidDescription", environment.getProperty("Niepoprawny opis transakcji!"));
    }

}
