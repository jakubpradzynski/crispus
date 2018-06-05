package pl.jakubpradzynski.crispus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.jakubpradzynski.crispus.dto.AccountValuesDto;
import pl.jakubpradzynski.crispus.dto.ChangeAccountNameDto;
import pl.jakubpradzynski.crispus.dto.NewAccountDto;
import pl.jakubpradzynski.crispus.dto.RemoveAccountDto;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;
import pl.jakubpradzynski.crispus.services.AccountService;
import pl.jakubpradzynski.crispus.services.DataService;
import pl.jakubpradzynski.crispus.utils.SessionUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static pl.jakubpradzynski.crispus.utils.RequestUtils.isErrorOccured;

/**
 * A controller-type class for handling account-related requests.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Controller
public class AccountsController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DataService dataService;

    @Autowired
    private Environment environment;

    /**
     * Method supports request GET for a path "/accounts".
     * Adds the necessary data related to user accounts to the model.
     * @param model - Model from MVC
     * @return Model and View (accounts.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ModelAndView showAccounts(Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addElementsToModel(model);
        return new ModelAndView("accounts", "model", model);
    }

    /**
     * Method supports request POST for a path "/account/edit".
     * Validates data from the user and, depending on the result, changes the account name or returns an error.
     * @param changeAccountNameDto - data from user with new account name
     * @param result - BindingResult
     * @param model - Model from MVC
     * @return Model and View (accounts.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/account/edit", method = RequestMethod.POST)
    public ModelAndView editAccountName(@ModelAttribute("newAccountName") @Valid ChangeAccountNameDto changeAccountNameDto, BindingResult result, Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        if (result.hasErrors()) {
            model.addAttribute("errors");
            addElementsToModel(model);
            return new ModelAndView("/accounts", "model", model);
        }
        String username = (String) httpSession.getAttribute("username");
        accountService.changeUserAccountName(username, changeAccountNameDto);
        return new ModelAndView("redirect:/accounts");
    }

    /**
     * Method supports request POST for a path "/account/new".
     * Validates data from the user and, depending on the result, add new account or returns an error.
     * @param newAccountDto - data for new account
     * @param result - BindingResult
     * @param model - Model from MVC
     * @param errors - Errors
     * @return Model and View (accounts.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/account/new", method = RequestMethod.POST)
    public ModelAndView addNewAccount(@ModelAttribute("newAccountName") @Valid NewAccountDto newAccountDto, BindingResult result, Model model, Errors errors) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        if (result.hasErrors()) {
            addErrorsAttributesToModel(errors, model);
            addElementsToModel(model);
            return new ModelAndView("/accounts", "model", model);
        }
        newAccountDto.setUsername((String) httpSession.getAttribute("username"));
        accountService.addNewUserAccount(newAccountDto);
        return new ModelAndView("redirect:/accounts");
    }

    /**
     * Method supports request POST for a path "/account/remove".
     * Checks whether the session has expired.
     * Validates data from the user and, depending on the result, remove account or returns an error.
     * @param removeAccountDto - data about removing account
     * @param result - BindingResult
     * @param model - Model from MVC
     * @param errors - Errors
     * @return Model and View (accounts.html)
     */
    @RequestMapping(value = "/account/remove", method = RequestMethod.POST)
    public ModelAndView removeAccount(@ModelAttribute("removeAccountDto") @Valid RemoveAccountDto removeAccountDto, BindingResult result, Model model, Errors errors) {
        SessionUtils.isUserSessionActive(httpSession);
        if (result.hasErrors()) {
            addErrorsAttributesToModel(errors, model);
            addElementsToModel(model);
            return new ModelAndView("/accounts", "model", model);
        }
        removeAccountDto.setUsername((String) httpSession.getAttribute("username"));
        accountService.removeUserAccount(removeAccountDto);
        return new ModelAndView("redirect:/accounts");
    }

    /**
     * Private method which add elements to model
     * Adds to model data related with user accounts.
     * @param model - Model from MVC
     */
    private void addElementsToModel(Model model) {
        String username = (String) httpSession.getAttribute("username");
        List<String> accountsNames = dataService.getUserAccountsNames(username);
        Collections.sort(accountsNames, String.CASE_INSENSITIVE_ORDER);
        List<AccountValuesDto> accountValuesDtos = accountService.getAccountsValuesDto(username, accountsNames);
        model.addAttribute("usedAccounts", accountService.getUserUsedAccountsNumber(username));
        model.addAttribute("availableAccounts", accountService.getUserAvailableAccountNumber(username));
        model.addAttribute("accountsValues", accountValuesDtos);
        model.addAttribute("changeAccountNameDto", new ChangeAccountNameDto());
        model.addAttribute("newAccountDto", new NewAccountDto());
        model.addAttribute("removeAccountDto", new RemoveAccountDto());
    }

    /**
     * Private method that adds errors to the model.
     * @param errors - Errors
     * @param model - Model form MVC
     */
    private void addErrorsAttributesToModel(Errors errors, Model model) {
        if (isErrorOccured(errors, "name")) model.addAttribute("invalidAccountName", environment.getProperty("Niepoprawna nazwa konta!"));
        if (isErrorOccured(errors, "moneyAmount")) model.addAttribute("invalidAccountAmount", environment.getProperty("Niepoprawna kwota na koncie!"));
        if (isErrorOccured(errors, "username")) model.addAttribute("usernameError", environment.getProperty("Wystąpił jakiś błąd z nazwą użytkownika!"));
    }
}
