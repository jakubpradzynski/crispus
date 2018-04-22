package pl.jakubpradzynski.crispus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
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

import static pl.jakubpradzynski.crispus.utils.RequestUtils.isErrorOccured;

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

    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public String showTransactions() throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        return "transactions";
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public ModelAndView showTransactionById(@RequestParam("id") Integer id) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        TransactionDto transactionDto = transactionService.getTransactionDtoById(id);
        return new ModelAndView("transaction", "transaction", transactionDto);
    }

    @RequestMapping(value = "/transaction/edit/{id}", method = RequestMethod.GET)
    public ModelAndView showEditTransactionById(@PathVariable("id") Integer id, Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        TransactionDto transactionDto = transactionService.getTransactionDtoById(id);
        model.addAttribute("transaction", transactionDto);
        addAttributesToModel(model);
        return new ModelAndView("transactionEdit", "model", model);
    }

    @RequestMapping(value = "/transaction/edit", method = RequestMethod.POST)
    public ModelAndView editTransactionById(@ModelAttribute("transaction") @Valid TransactionDto transactionDto, BindingResult result, WebRequest request, Errors errors, Model model) throws SessionExpiredException, ParseException {
        SessionUtils.isUserSessionActive(httpSession);
        System.out.println(transactionDto);
        System.out.println(errors);
        if (result.hasErrors()) {
            addErrorsAttributesToModel(errors, model);
            addAttributesToModel(model);
            return new ModelAndView("redirect:/transaction/edit/" + transactionDto.getId(), "model", model);
        }
        System.out.println("jestem tu 1");
        transactionService.editTransactionById(transactionDto.getId(), transactionDto);
        System.out.println("jestem tu 8");
        return new ModelAndView("redirect:/transaction?id=" + transactionDto.getId());
    }

    private void addAttributesToModel(Model model) {
        List<String> accountNames = dataService.getUserAccountsNames((String) httpSession.getAttribute("username"));
        Collections.sort(accountNames, String.CASE_INSENSITIVE_ORDER);
        List<String> placesDescriptions = dataService.getPlacesDescriptionsAvailableForUser((String) httpSession.getAttribute("username"));
        Collections.sort(placesDescriptions, String.CASE_INSENSITIVE_ORDER);
        List<String> transactionCategoriesNames = dataService.getTransactionCategoriesNamesAvailableForUser((String) httpSession.getAttribute("username"));
        Collections.sort(transactionCategoriesNames, String.CASE_INSENSITIVE_ORDER);
        model.addAttribute("userAccountsNames", accountNames);
        model.addAttribute("placesDescriptions", placesDescriptions);
        model.addAttribute("transactionCategoriesNames", transactionCategoriesNames);
    }

    private void addErrorsAttributesToModel(Errors errors, Model model) {
        if (isErrorOccured(errors, "value")) model.addAttribute("invalidValue", environment.getProperty("invalid.value"));
        if (isErrorOccured(errors, "description")) model.addAttribute("invalidDescription", environment.getProperty("invalid.description"));
    }

}
