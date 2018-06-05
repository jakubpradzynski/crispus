package pl.jakubpradzynski.crispus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.jakubpradzynski.crispus.dto.MonthlyBudgetInfoDto;
import pl.jakubpradzynski.crispus.dto.NewMonthlyBudgetDto;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;
import pl.jakubpradzynski.crispus.services.MonthlyBudgetService;
import pl.jakubpradzynski.crispus.utils.SessionUtils;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

/**
 * A controller-type class for handling budget-related requests.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Controller
public class BudgetsController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private MonthlyBudgetService monthlyBudgetService;

    /**
     * Method supports request GET for a path "/budgets".
     * Adds the necessary data related to user budgets to the model.
     * @param model - Model from MVC
     * @return Model and View (budgets.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/budgets", method = RequestMethod.GET)
    public ModelAndView showBudgets(Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addAttributesToModel(model);
        return new ModelAndView("budgets", "model", model);
    }

    /**
     * Method supports request POST for a path "/budget/add".
     * Validates data from the user and, depending on the result, add new budget or returns an error.
     * @param newMonthlyBudgetDto - data about new user budget
     * @return Model and View (budgets.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     * @throws ParseException - Exception is thrown when it is impossible to parse the date from the string.
     */
    @RequestMapping(value = "/budget/add", method = RequestMethod.POST)
    public String addNewBudget(@ModelAttribute("newBudget") NewMonthlyBudgetDto newMonthlyBudgetDto) throws SessionExpiredException, ParseException {
        SessionUtils.isUserSessionActive(httpSession);
        if (newMonthlyBudgetDto.getAmount() == null || newMonthlyBudgetDto.getAmount() < 1.) return "redirect:/budgets";
        newMonthlyBudgetDto.setStartDate(LocalDate.now().withDayOfMonth(1).toString());
        newMonthlyBudgetDto.setEndDate(LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1).toString());
        monthlyBudgetService.addNewUserMonthlyBudget((String) httpSession.getAttribute("username"), newMonthlyBudgetDto);
        return "redirect:/budgets";
    }

    /**
     * Method supports request GET for a path "/budget/transactions/{id}".
     * Show transactions related to budget specific by id.
     * @param id - budget id
     * @param model - Model from MVC
     * @return Model and View (budgetTransactions.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/budget/transactions/{id}", method = RequestMethod.GET)
    public ModelAndView showBudgetTransactions(@PathVariable("id") Integer id, Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        MonthlyBudgetInfoDto monthlyBudgetInfoDto = monthlyBudgetService.getBudgetInfoById(id);
        model.addAttribute("budget", monthlyBudgetInfoDto);
        model.addAttribute("budgetTransacions", monthlyBudgetService.getUserBudgetTransactionsList((String) httpSession.getAttribute("username") ,monthlyBudgetInfoDto));
        return new ModelAndView("budgetTransactions", "model", model);
    }

    /**
     * Method supports request GET for a path "/budget/{id}".
     * Show budget information specific by id.
     * @param id - budget id
     * @param model - Model from MVC
     * @return Model and View (budget.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/budget/{id}", method = RequestMethod.GET)
    public ModelAndView showBudget(@PathVariable("id") Integer id, Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        MonthlyBudgetInfoDto monthlyBudgetInfoDto = monthlyBudgetService.getBudgetInfoById(id);
        model.addAttribute("budget", monthlyBudgetInfoDto);
        return new ModelAndView("budget", "model", model);
    }

    /**
     * Private method which add attributes to model
     * Adds to model data related with user budgets.
     * @param model - Model from MVC
     */
    private void addAttributesToModel(Model model) {
        String username = (String) httpSession.getAttribute("username");
        MonthlyBudgetInfoDto actualBudget = monthlyBudgetService.getUserActualMonthlyBudgetDto(username);
        List<MonthlyBudgetInfoDto> lastBudgets = monthlyBudgetService.getUserLastMonthlyBudgetsDto(username);
        model.addAttribute("newBudget", new NewMonthlyBudgetDto());
        model.addAttribute("actualBudget", actualBudget);
        model.addAttribute("lastBudgets", lastBudgets);
    }

}
