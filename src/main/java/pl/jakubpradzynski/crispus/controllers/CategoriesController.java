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
import pl.jakubpradzynski.crispus.dto.CategoryDto;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;
import pl.jakubpradzynski.crispus.exceptions.CategoryExistsException;
import pl.jakubpradzynski.crispus.services.CategoryService;
import pl.jakubpradzynski.crispus.utils.SessionUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static pl.jakubpradzynski.crispus.utils.RequestUtils.isErrorOccurred;

/**
 * A controller-type class for handling category-related requests.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Controller
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private Environment environment;

    /**
     * Method supports request GET for a path "/categories".
     * Adds the necessary data related to categories available for user to the model.
     * @param model - Model from MVC.
     * @return Model and View (categories.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ModelAndView showCategories(Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addAttributesToModel(model);
        return new ModelAndView("categories", "model", model);
    }

    /**
     * Method supports request POST for a path "/category/add".
     * Validates data from the user and, depending on the result, add new user category or returns an error.
     * @param categoryDto - info from user about new category.
     * @param model - Model from MVC.
     * @param result - BidningResult.
     * @param errors - Errors.
     * @return Model and View (categories.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     * @throws CategoryExistsException - Exception thrown when category already exists.
     */
    @RequestMapping(value = "/category/add", method = RequestMethod.POST)
    public ModelAndView addNewCategory(@ModelAttribute("newCategory") @Valid CategoryDto categoryDto, Model model, BindingResult result, Errors errors) throws SessionExpiredException, CategoryExistsException {
        SessionUtils.isUserSessionActive(httpSession);
        if (result.hasErrors()) {
            model.addAttribute("newCategory", categoryDto);
            addErrorsAttributesToModel(errors, model);
            addAttributesToModel(model);
            return new ModelAndView("/categories", "model", model);
        }
        String username = (String) httpSession.getAttribute("username");
        categoryService.addNewUserCategory(username, categoryDto);
        return new ModelAndView("redirect:/categories");
    }

    /**
     * Method supports request GET for a path "/category/{id}/{size}".
     * Method return view with information about category specific by id.
     * Size is responsible for the range of transactions displayed.
     * @param id - category id
     * @param size - category transactions range
     * @param model - Model from MVC
     * @return Model and View (category.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/category/{id}/{size}", method = RequestMethod.GET)
    public ModelAndView showSingleCategory(@PathVariable("id") Integer id, @PathVariable("size") Integer size, Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addAttributesToModelOfSingleCategory(id, size, model);
        return new ModelAndView("category", "model", model);
    }

    /**
     * Method supports request GET for a path "/category/remove/{id}".
     * Checks whether the session has expired.
     * Mehod remove user category specific by id.
     * @param id - category id
     * @return String (redirect:/categories.html)
     */
    @RequestMapping(value = "/category/remove/{id}", method = RequestMethod.GET)
    public String removeCategory(@PathVariable("id") Integer id) {
        SessionUtils.isUserSessionActive(httpSession);
        String username = (String) httpSession.getAttribute("username");
        categoryService.removeUserCategory(username, id);
        return "redirect:/categories";
    }

    /**
     * Method supports request POST for a path "/category/changeName/{id}".
     * Checks whether the session has expired.
     * Validates data from the user and, depending on the result, add change user category or returns an error.
     * @param id - category id
     * @param categoryDto - new category data
     * @param model - Model from MVC
     * @param result - BindingResult
     * @param errors - Errors
     * @return Model and View (category.html)
     * @throws CategoryExistsException - Exception thrown when category already exists.
     */
    @RequestMapping(value = "/category/changeName/{id}", method = RequestMethod.POST)
    public ModelAndView changeCategoryName(@PathVariable("id") Integer id, @ModelAttribute("editedCategory") @Valid CategoryDto categoryDto, Model model, BindingResult result, Errors errors) throws CategoryExistsException {
        SessionUtils.isUserSessionActive(httpSession);
        String username = (String) httpSession.getAttribute("username");
        if (result.hasErrors()) {
            addErrorsAttributesToModel(errors, model);
            addAttributesToModelOfSingleCategory(id, 1, model);
            return new ModelAndView("/category/" + id + "/1", "model", model);
        }
        categoryDto.setId(id);
        Integer newId = categoryService.changeUserCategoryName(username, categoryDto);
        if (newId != -1) id = newId;
        return new ModelAndView("redirect:/category/" + id + "/1");
    }

    /**
     * Method add necessary attributes related with single category to model.
     * @param id - category id
     * @param size - category transactions range
     * @param model - Model from MVC
     */
    private void addAttributesToModelOfSingleCategory(Integer id, Integer size, Model model) {
        String username = (String) httpSession.getAttribute("username");
        CategoryDto categoryDto = categoryService.getTransactionCategoryDtoById(id);
        List<TransactionDto> categoryTransactionsDto = categoryService.getUserCategoryTransactionsDtoByIdInRange(username, id, (size - 1) * 10, 10);
        model.addAttribute("category", categoryDto);
        model.addAttribute("categoryTransactionsDto", categoryTransactionsDto);
        model.addAttribute("editedCategory", new CategoryDto());
        model.addAttribute("pathSize", size);
    }

    /**
     * Method add necessary attributes related with user categories to model.
     * @param model - Model from MVC
     */
    private void addAttributesToModel(Model model) {
        String username = (String) httpSession.getAttribute("username");
        Integer usedCategories = categoryService.getUserUsedCategoriesNumber(username);
        Integer availableCategories = categoryService.getUserAvailableCategoriesNumber(username);
        List<CategoryDto> preDefinedCategories = categoryService.getPreDefinedCategories();
        Collections.sort(preDefinedCategories);
        List<CategoryDto> userCreatedCategories = categoryService.getUserCreatedCategories(username);
        Collections.sort(userCreatedCategories);
        model.addAttribute("usedCategories", usedCategories);
        model.addAttribute("availableCategories", availableCategories);
        model.addAttribute("preDefinedCategories", preDefinedCategories);
        model.addAttribute("userCreatedCategories", userCreatedCategories);
        model.addAttribute("newCategory", new CategoryDto());
    }

    /**
     * Method add specific occurred errors to model
     * @param errors - Errors
     * @param model - Model from MVC
     */
    private void addErrorsAttributesToModel(Errors errors, Model model) {
        if (isErrorOccurred(errors, "name")) model.addAttribute("invalidCategoryName", environment.getProperty("Niepoprawna nazwa kategorii!"));
    }
}
