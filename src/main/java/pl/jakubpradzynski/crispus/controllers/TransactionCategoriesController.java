package pl.jakubpradzynski.crispus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.jakubpradzynski.crispus.dto.PlaceDto;
import pl.jakubpradzynski.crispus.dto.TransactionCategoryDto;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.exceptions.PlaceExistsException;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;
import pl.jakubpradzynski.crispus.exceptions.TransactionCategoryExistsException;
import pl.jakubpradzynski.crispus.services.PlaceService;
import pl.jakubpradzynski.crispus.services.TransactionCategoryService;
import pl.jakubpradzynski.crispus.utils.SessionUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static pl.jakubpradzynski.crispus.utils.RequestUtils.isErrorOccured;

@Controller
public class TransactionCategoriesController {

    @Autowired
    TransactionCategoryService transactionCategoryService;

    @Autowired
    HttpSession httpSession;

    @Autowired
    Environment environment;

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ModelAndView showCategories(Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addAttributesToModel(model);
        return new ModelAndView("categories", "model", model);
    }

    @RequestMapping(value = "/category/add", method = RequestMethod.POST)
    public ModelAndView addNewCategory(@ModelAttribute("newCategory") @Valid TransactionCategoryDto transactionCategoryDto, Model model, BindingResult result, Errors errors) throws SessionExpiredException, TransactionCategoryExistsException {
        SessionUtils.isUserSessionActive(httpSession);
        if (result.hasErrors()) {
            model.addAttribute("newCategory", transactionCategoryDto);
            addErrorsAttributesToModel(errors, model);
            addAttributesToModel(model);
            return new ModelAndView("/categories", "model", model);
        }
        String username = (String) httpSession.getAttribute("username");
        transactionCategoryService.addNewUserCategory(username, transactionCategoryDto);
        return new ModelAndView("redirect:/categories");
    }

    @RequestMapping(value = "/category/{id}/{size}", method = RequestMethod.GET)
    public ModelAndView showSingleCategory(@PathVariable("id") Integer id, @PathVariable("size") Integer size, Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addAttributesToModelOfSingleCategory(id, size, model);
        return new ModelAndView("category", "model", model);
    }

    @RequestMapping(value = "/category/remove/{id}", method = RequestMethod.GET)
    public String removeCategory(@PathVariable("id") Integer id) {
        SessionUtils.isUserSessionActive(httpSession);
        String username = (String) httpSession.getAttribute("username");
        transactionCategoryService.removeUserCategory(username, id);
        return "redirect:/categories";
    }

    @RequestMapping(value = "/category/changeName/{id}", method = RequestMethod.POST)
    public ModelAndView changeCategoryName(@PathVariable("id") Integer id, @ModelAttribute("editedCategory") @Valid TransactionCategoryDto transactionCategoryDto, Model model, BindingResult result, Errors errors) throws  TransactionCategoryExistsException {
        SessionUtils.isUserSessionActive(httpSession);
        String username = (String) httpSession.getAttribute("username");
        if (result.hasErrors()) {
            addErrorsAttributesToModel(errors, model);
            addAttributesToModelOfSingleCategory(id, 1, model);
            return new ModelAndView("/category/" + id + "/1", "model", model);
        }
        transactionCategoryDto.setId(id);
        Integer newId = transactionCategoryService.changeUserCategoryName(username, transactionCategoryDto);
        if (newId != -1) id = newId;
        return new ModelAndView("redirect:/category/" + id + "/1");
    }

    private void addAttributesToModelOfSingleCategory(Integer id, Integer size, Model model) {
        String username = (String) httpSession.getAttribute("username");
        TransactionCategoryDto transactionCategoryDto = transactionCategoryService.getTransactionCategoryDtoById(id);
        List<TransactionDto> categoryTransactionsDto = transactionCategoryService.getUserCategoryTransactionsDtoByIdInRange(username, id, (size - 1) * 10, 10);
        model.addAttribute("category", transactionCategoryDto);
        model.addAttribute("categoryTransactionsDto", categoryTransactionsDto);
        model.addAttribute("editedCategory", new TransactionCategoryDto());
        model.addAttribute("pathSize", size);
    }

    private void addAttributesToModel(Model model) {
        String username = (String) httpSession.getAttribute("username");
        Integer usedCategories = transactionCategoryService.getUserUsedCategoriesNumber(username);
        Integer availableCategories = transactionCategoryService.getUserAvailableCategoriesNumber(username);
        List<TransactionCategoryDto> preDefinedCategories = transactionCategoryService.getPreDefinedCategories();
        Collections.sort(preDefinedCategories);
        List<TransactionCategoryDto> userCreatedCategories = transactionCategoryService.getUserCreatedCategories(username);
        Collections.sort(userCreatedCategories);
        model.addAttribute("usedCategories", usedCategories);
        model.addAttribute("availableCategories", availableCategories);
        model.addAttribute("preDefinedCategories", preDefinedCategories);
        model.addAttribute("userCreatedCategories", userCreatedCategories);
        model.addAttribute("newCategory", new TransactionCategoryDto());
    }

    private void addErrorsAttributesToModel(Errors errors, Model model) {
        if (isErrorOccured(errors, "name")) model.addAttribute("invalidCategoryName", environment.getProperty("invalid.category.name"));
    }
}
