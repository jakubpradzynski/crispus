package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.domain.Place;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.PlaceDto;
import pl.jakubpradzynski.crispus.repositories.PlaceRepository;
import pl.jakubpradzynski.crispus.repositories.UserRepository;
import pl.jakubpradzynski.crispus.repositories.UserTypeRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PlaceService {

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    public Integer getUserUsedPlacesNumber(String username) {
        User user = userRepository.getUserByEmail(username);
        return placeRepository.getUserUsedPlacesNumber(user);
    }

    public Integer getUserAvailablePlacesNumber(String username) {
        User user = userRepository.getUserByEmail(username);
        return userTypeRepository.getPlaceNumberAvailableForUser(user);
    }

    public List<PlaceDto> getPreDefinedPlaces() {
        Collection<Place> allPreDefinedPlaces = placeRepository.getAllPreDefinedPlaces();
        List<PlaceDto> preDefinedPlacesDto = new ArrayList<>();
        allPreDefinedPlaces.forEach(place -> preDefinedPlacesDto.add(PlaceDto.fromPlace(place)));
        return preDefinedPlacesDto;
    }

    public List<PlaceDto> getUserCreatedPlaces(String username) {
        User user = userRepository.getUserByEmail(username);
        Collection<Place> userCreatedPlaces = placeRepository.getPlaceCreatedByUser(user);
        List<PlaceDto> userCreatePlacesDto = new ArrayList<>();
        userCreatedPlaces.forEach(place -> userCreatePlacesDto.add(PlaceDto.fromPlace(place)));
        return userCreatePlacesDto;
    }
}
