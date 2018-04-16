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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;
import pl.jakubpradzynski.crispus.services.DataService;
import pl.jakubpradzynski.crispus.services.TransactionService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static pl.jakubpradzynski.crispus.utils.RequestUtils.isErrorOccured;

@Controller
@PropertySource("classpath:/i18n/messages_pl.properties")
public class HomepageController {

    @Autowired
    DataService dataService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    HttpSession httpSession;

    @Autowired
    private Environment environment;

    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public String showHomepage(Model model) throws SessionExpiredException {
        if (httpSession.getAttribute("username") == null) {
            throw new SessionExpiredException("Sesja wygasła!");
        }
        addAttributesToModel(model);
        return "homepage.html";
    }

    @RequestMapping(value = "/homepage", method = RequestMethod.POST)
    public ModelAndView addNewTransaction
            (@ModelAttribute("newTransactionDto") @Valid TransactionDto transactionDto,
             BindingResult result, WebRequest request, Errors errors, Model model) throws Exception {

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

    private void addAttributesToModel(Model model) {
        model.addAttribute("userPublicData", dataService.getPublicUserData((String) httpSession.getAttribute("username")));
        model.addAttribute("lastTwentyTransactionsDto", dataService.getUserLastTwentyTransactionsDto((String) httpSession.getAttribute("username")));
        model.addAttribute("newTransactionDto", new TransactionDto((String) httpSession.getAttribute("username")));
        model.addAttribute("userAccountsNames", dataService.getUserAccountsNames((String) httpSession.getAttribute("username")));
        model.addAttribute("placesDescriptions", dataService.getPlacesDescriptionsAvailableForUser((String) httpSession.getAttribute("username")));
        model.addAttribute("transactionCategoriesNames", dataService.getTransactionCategoriesNamesAvailableForUser((String) httpSession.getAttribute("username")));
    }

    private void addErrorsAttributesToModel(Errors errors, Model model) {
        if (isErrorOccured(errors, "value")) model.addAttribute("invalidValue", environment.getProperty("invalid.value"));
        if (isErrorOccured(errors, "description")) model.addAttribute("invalidDescription", environment.getProperty("invalid.description"));
    }

}