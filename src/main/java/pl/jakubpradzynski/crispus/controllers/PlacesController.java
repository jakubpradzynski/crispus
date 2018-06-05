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
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.exceptions.PlaceExistsException;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;
import pl.jakubpradzynski.crispus.services.PlaceService;
import pl.jakubpradzynski.crispus.utils.SessionUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static pl.jakubpradzynski.crispus.utils.RequestUtils.isErrorOccured;

/**
 * A controller-type class for handling place-related requests.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Controller
public class PlacesController {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private Environment environment;

    /**
     * Method supports request GET for a path "/places".
     * Adds necessary info about user place to model.
     * @param model - Model from MVC
     * @return Model and View (places.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/places", method = RequestMethod.GET)
    public ModelAndView showPlaces(Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addAttributesToModel(model);
        return new ModelAndView("places", "model", model);
    }

    /**
     * Method supports request POST for a path "/place/add".
     * Validates data from the user and, depending on the result, add new user place or returns an error.
     * @param placeDto - info about new place from user
     * @param model - Model from MVC
     * @param result - BindingResult
     * @param errors - Errors
     * @return Model and View (places.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     * @throws PlaceExistsException - Exception thrown when place already exists.
     */
    @RequestMapping(value = "/place/add", method = RequestMethod.POST)
    public ModelAndView addNewPlace(@ModelAttribute("newPlace") @Valid PlaceDto placeDto, Model model, BindingResult result, Errors errors) throws SessionExpiredException, PlaceExistsException {
        SessionUtils.isUserSessionActive(httpSession);
        if (result.hasErrors()) {
            model.addAttribute("newPlace", placeDto);
            addErrorsAttributesToModel(errors, model);
            addAttributesToModel(model);
            return new ModelAndView("/places", "model", model);
        }
        String username = (String) httpSession.getAttribute("username");
        placeService.addNewUserPlace(username, placeDto);
        return new ModelAndView("redirect:/places");
    }

    /**
     * Method supports request GET for a path "/place/{id}/{size}".
     * Method return view with information about place specific by id.
     * Size is responsible for the range of transactions displayed.
     * @param id - place id
     * @param size - place transactions range
     * @param model - Model from MVC
     * @return Model and View (place.html)
     * @throws SessionExpiredException - Checks whether the session has expired.
     */
    @RequestMapping(value = "/place/{id}/{size}", method = RequestMethod.GET)
    public ModelAndView showSinglePlace(@PathVariable("id") Integer id, @PathVariable("size") Integer size, Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addAttributesToModelOfSinglePlace(id, size, model);
        return new ModelAndView("place", "model", model);
    }

    /**
     * Method supports request GET for a path "/place/remove/{id}".
     * Checks whether the session has expired.
     * Remove place specific by id.
     * @param id - place id
     * @return Model and View (places.html)
     */
    @RequestMapping(value = "/place/remove/{id}", method = RequestMethod.GET)
    public String removePlace(@PathVariable("id") Integer id) {
        SessionUtils.isUserSessionActive(httpSession);
        String username = (String) httpSession.getAttribute("username");
        placeService.removeUserPlace(username, id);
        return "redirect:/places";
    }

    /**
     * Method supports request POST for a path "/place/changeName/{id}".
     * Checks whether the session has expired.
     * Validates data from the user and, depending on the result, add change user place name or returns an error.
     * @param id - place id
     * @param placeDto - new place data
     * @param model - Model from MVC
     * @param result - BindingResult
     * @param errors - Errors
     * @return Model and View (place.html)
     * @throws PlaceExistsException - Exception thrown when place already exists.
     */
    @RequestMapping(value = "/place/changeName/{id}", method = RequestMethod.POST)
    public ModelAndView changePlaceName(@PathVariable("id") Integer id, @ModelAttribute("editedPlace") @Valid PlaceDto placeDto, Model model, BindingResult result, Errors errors) throws PlaceExistsException {
        SessionUtils.isUserSessionActive(httpSession);
        String username = (String) httpSession.getAttribute("username");
        if (result.hasErrors()) {
            addErrorsAttributesToModel(errors, model);
            addAttributesToModelOfSinglePlace(id, 1, model);
            return new ModelAndView("/place/" + id + "/1", "model", model);
        }
        placeDto.setId(id);
        Integer newId = placeService.changeUserPlaceName(username, placeDto);
        if (newId != -1) id = newId;
        return new ModelAndView("redirect:/place/" + id + "/1");
    }

    /**
     * Method add necessary data about single place specific by id to model.
     * @param id - place id
     * @param size - place transactions range
     * @param model - Model from MVC
     */
    private void addAttributesToModelOfSinglePlace(Integer id, Integer size, Model model) {
        String username = (String) httpSession.getAttribute("username");
        PlaceDto placeDto = placeService.getPlaceDtoById(id);
        List<TransactionDto> placeTransactionsDto = placeService.getUserPlaceTransactionsDtoByIdInRange(username, id, (size - 1) * 10, 10);
        model.addAttribute("place", placeDto);
        model.addAttribute("placeTransactionsDto", placeTransactionsDto);
        model.addAttribute("editedPlace", new PlaceDto());
        model.addAttribute("pathSize", size);
    }

    /**
     * Method add necessary data about user places to model.
     * @param model - Model from MVC
     */
    private void addAttributesToModel(Model model) {
        String username = (String) httpSession.getAttribute("username");
        Integer usedPlaces = placeService.getUserUsedPlacesNumber(username);
        Integer availablePlaces = placeService.getUserAvailablePlacesNumber(username);
        List<PlaceDto> preDefinedPlaces = placeService.getPreDefinedPlaces();
        Collections.sort(preDefinedPlaces);
        List<PlaceDto> userCreatedPlaces = placeService.getUserCreatedPlaces(username);
        Collections.sort(userCreatedPlaces);
        model.addAttribute("usedPlaces", usedPlaces);
        model.addAttribute("availablePlaces", availablePlaces);
        model.addAttribute("preDefinedPlaces", preDefinedPlaces);
        model.addAttribute("userCreatedPlaces", userCreatedPlaces);
        model.addAttribute("newPlace", new PlaceDto());
    }

    /**
     * Method add to model specific error which occurred.
     * @param errors - Errors
     * @param model - Model from MVC
     */
    private void addErrorsAttributesToModel(Errors errors, Model model) {
        if (isErrorOccured(errors, "name")) model.addAttribute("invalidPlaceName", environment.getProperty("Niepoprawna nazwa miejsca!"));
    }
}
