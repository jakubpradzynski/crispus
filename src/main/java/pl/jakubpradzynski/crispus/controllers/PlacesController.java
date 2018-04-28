package pl.jakubpradzynski.crispus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.jakubpradzynski.crispus.domain.Place;
import pl.jakubpradzynski.crispus.dto.PlaceDto;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;
import pl.jakubpradzynski.crispus.services.PlaceService;
import pl.jakubpradzynski.crispus.utils.SessionUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PlacesController {

    @Autowired
    PlaceService placeService;

    @Autowired
    HttpSession httpSession;

    @RequestMapping(value = "/places", method = RequestMethod.GET)
    public ModelAndView showAccounts(Model model) throws SessionExpiredException {
        SessionUtils.isUserSessionActive(httpSession);
        addAttributesToModel(model);
        return new ModelAndView("attributes", "model", model);
    }

    private void addAttributesToModel(Model model) {
        String username = (String) httpSession.getAttribute("username");
        Integer usedPlaces = placeService.getUserUsedPlacesNumber(username);
        Integer availablePlaces = placeService.getUserAvailablePlacesNumber(username);
        List<PlaceDto> preDefinedPlaces = placeService.getPreDefinedPlaces();
        List<PlaceDto> userCreatedPlaces = placeService.getUserCreatedPlaces(username);
        model.addAttribute("usedPlaces", usedPlaces);
        model.addAttribute("availablePlaces", availablePlaces);
        model.addAttribute("preDefinedPlaces", preDefinedPlaces);
        model.addAttribute("userCreatedPlaces", userCreatedPlaces);
    }
}
