package pl.jakubpradzynski.crispus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
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
 * A controller-type class for handling transaction-related requests.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Controller
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private Environment environment;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private DataService dataService;

    /**
     * Method supports request GET for a path "/transactions/{id}".
     * Shows user transactions page after added necessary data about transactions to model.
     * @param id - transactions range
     * @param model - Model from MVC
     * @return Model and View (transactions.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/transactions/{id}", method = RequestMethod.GET)
    public ModelAndView showTransactions(@PathVariable("id") Integer id, Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addAttributesToModel(model);
        addAttributesToModel(id, model);
        model.addAttribute("newTransactionDto", new TransactionDto((String) httpSession.getAttribute("username")));
        return new ModelAndView("transactions", "model", model);
    }

    /**
     * Method supports request GET for a path "/transaction".
     * Shows info about user transaction specific by id.
     * @param id - transaction id
     * @return Model and View (transaction.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public ModelAndView showTransactionById(@RequestParam("id") Integer id) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        TransactionDto transactionDto = transactionService.getTransactionDtoById(id);
        return new ModelAndView("transaction", "transaction", transactionDto);
    }

    /**
     * Method supports request POST for a path "/transaction/add".
     * Validates data from the user and, depending on the result, add new user transactions or returns an error.
     * @param transactionDto - transaction data from user
     * @param result - BindingResult
     * @param errors - Errors
     * @param model - Model from MVC
     * @return Model and View (transactions.html)
     * @throws ParseException - Exception is thrown when it is impossible to parse the date from the string.
     */
    @RequestMapping(value = "/transaction/add", method = RequestMethod.POST)
    public ModelAndView addNewTransaction
            (@ModelAttribute("newTransactionDto") @Valid TransactionDto transactionDto,
             BindingResult result, Errors errors, Model model) throws ParseException {

        if (!result.hasErrors()) {
            transactionService.addNewUserTransaction(transactionDto);
        }

        if (result.hasErrors()) {
            model.addAttribute("newTransactionDto", transactionDto);
            addAttributesToModel(model);
            addAttributesToModel(1, model);
            addErrorsAttributesToModel(errors, model);
            return new ModelAndView("transactions", "model", model);
        }
        return new ModelAndView("redirect:/transactions/1");
    }

    /**
     * Method supports request GET for a path "/transaction/edit/{id}".
     * Shows edit page for transaction specific by id.
     * @param id - transaction id
     * @param model - Model from MVC
     * @return Model and View (transactionEdit.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/transaction/edit/{id}", method = RequestMethod.GET)
    public ModelAndView showEditTransactionById(@PathVariable("id") Integer id, Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        TransactionDto transactionDto = transactionService.getTransactionDtoById(id);
        model.addAttribute("transaction", transactionDto);
        addAttributesToModel(model);
        return new ModelAndView("transactionEdit", "model", model);
    }

    /**
     * Method supports request GET for a path "/transaction/remove/{id}".
     * Remove user transaction specific by id.
     * @param id - transaction id
     * @return String (homepage.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/transaction/remove/{id}", method = RequestMethod.GET)
    public String removeTransaction(@PathVariable("id") Integer id) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        transactionService.removeTransactionById(id);
        return "redirect:/homepage";
    }

    /**
     * Method supports request POST for a path "/transaction/edit".
     * Validates data from the user and, depending on the result, edit user transaction or returns an error.
     * @param transactionDto - new transaction data
     * @param result - BindingResult
     * @param errors - Errors
     * @param model - Model from MVC
     * @return Model and View (transaction.html when transaction was edited and transactionEdit.html otherwise)
     * @throws SessionExpiredException - Checks whether the session has expired.
     * @throws ParseException - Exception is thrown when it is impossible to parse the date from the string.
     */
    @RequestMapping(value = "/transaction/edit", method = RequestMethod.POST)
    public ModelAndView editTransactionById(@ModelAttribute("transaction") @Valid TransactionDto transactionDto, BindingResult result, Errors errors, Model model) throws SessionExpiredException, ParseException {
        SessionUtils.isUserSessionActive(httpSession);
        if (result.hasErrors()) {
            addErrorsAttributesToModel(errors, model);
            addAttributesToModel(model);
            return new ModelAndView("redirect:/transaction/edit/" + transactionDto.getId(), "model", model);
        }
        transactionService.editTransactionById(transactionDto.getId(), transactionDto);
        return new ModelAndView("redirect:/transaction?id=" + transactionDto.getId());
    }

    /**
     * Adds necessary attributes about user transactions to model.
     * @param model - Model from MVC
     */
    private void addAttributesToModel(Model model) {
        List<String> accountNames = dataService.getUserAccountsNames((String) httpSession.getAttribute("username"));
        Collections.sort(accountNames, String.CASE_INSENSITIVE_ORDER);
        List<String> placesNames = dataService.getPlacesNamesAvailableForUser((String) httpSession.getAttribute("username"));
        Collections.sort(placesNames, String.CASE_INSENSITIVE_ORDER);
        List<String> transactionCategoriesNames = dataService.getTransactionCategoriesNamesAvailableForUser((String) httpSession.getAttribute("username"));
        Collections.sort(transactionCategoriesNames, String.CASE_INSENSITIVE_ORDER);
        model.addAttribute("userAccountsNames", accountNames);
        model.addAttribute("placesNames", placesNames);
        model.addAttribute("transactionCategoriesNames", transactionCategoriesNames);
    }

    /**
     * Adds necessary attributes about user transaction specific by id to model.
     * @param id - transaction id
     * @param model - Model from MVC
     */
    private void addAttributesToModel(Integer id, Model model) {
        String username = (String) httpSession.getAttribute("username");
        List<TransactionDto> transactionDtoList = transactionService.getUserTransactionByRange(username, (id - 1) * 10, 10);
        model.addAttribute("transactions", transactionDtoList);
        model.addAttribute("pathId", id);
    }

    /**
     * Adds specific, occurred errors to model.
     * @param errors - Errors
     * @param model - Model from MVC
     */
    private void addErrorsAttributesToModel(Errors errors, Model model) {
        if (isErrorOccurred(errors, "value")) model.addAttribute("invalidValue", environment.getProperty("Niepoprawna kwota transakcji!"));
        if (isErrorOccurred(errors, "description")) model.addAttribute("invalidDescription", environment.getProperty("Niepoprawny opis transakcji!"));
    }

}
