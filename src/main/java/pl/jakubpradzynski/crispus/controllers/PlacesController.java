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

@Controller
public class PlacesController {

    @Autowired
    PlaceService placeService;

    @Autowired
    HttpSession httpSession;

    @Autowired
    Environment environment;

    @RequestMapping(value = "/places", method = RequestMethod.GET)
    public ModelAndView showPlaces(Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addAttributesToModel(model);
        return new ModelAndView("places", "model", model);
    }

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

    @RequestMapping(value = "/place/{id}/{size}", method = RequestMethod.GET)
    public ModelAndView showSinglePlace(@PathVariable("id") Integer id, @PathVariable("size") Integer size, Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addAttributesToModelOfSinglePlace(id, size, model);
        return new ModelAndView("place", "model", model);
    }

    @RequestMapping(value = "/place/remove/{id}", method = RequestMethod.GET)
    public String removePlace(@PathVariable("id") Integer id) {
        SessionUtils.isUserSessionActive(httpSession);
        String username = (String) httpSession.getAttribute("username");
        placeService.removeUserPlace(username, id);
        return "redirect:/places";
    }

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

    private void addAttributesToModelOfSinglePlace(Integer id, Integer size, Model model) {
        String username = (String) httpSession.getAttribute("username");
        PlaceDto placeDto = placeService.getPlaceDtoById(id);
        List<TransactionDto> placeTransactionsDto = placeService.getUserPlaceTransactionsDtoByIdInRange(username, id, (size - 1) * 10, 10);
        model.addAttribute("place", placeDto);
        model.addAttribute("placeTransactionsDto", placeTransactionsDto);
        model.addAttribute("editedPlace", new PlaceDto());
        model.addAttribute("pathSize", size);
    }

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

    private void addErrorsAttributesToModel(Errors errors, Model model) {
        if (isErrorOccured(errors, "name")) model.addAttribute("invalidPlaceName", environment.getProperty("invalid.place.name"));
    }
}
