package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.domain.Place;
import pl.jakubpradzynski.crispus.domain.Transaction;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.PlaceDto;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.exceptions.PlaceExistsException;
import pl.jakubpradzynski.crispus.repositories.PlaceRepository;
import pl.jakubpradzynski.crispus.repositories.TransactionRepository;
import pl.jakubpradzynski.crispus.repositories.UserRepository;
import pl.jakubpradzynski.crispus.repositories.UserTypeRepository;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class PlaceService {

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    @Autowired
    TransactionRepository transactionRepository;

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
        if (allPreDefinedPlaces.size() > 0) allPreDefinedPlaces.forEach(place -> preDefinedPlacesDto.add(PlaceDto.fromPlace(place)));
        return preDefinedPlacesDto;
    }

    public List<PlaceDto> getUserCreatedPlaces(String username) {
        User user = userRepository.getUserByEmail(username);
        Collection<Place> userCreatedPlaces = placeRepository.getPlaceCreatedByUser(user);
        List<PlaceDto> userCreatePlacesDto = new ArrayList<>();
        userCreatedPlaces.forEach(place -> userCreatePlacesDto.add(PlaceDto.fromPlace(place)));
        return userCreatePlacesDto;
    }

    @Transactional
    public void addNewUserPlace(String username, PlaceDto placeDto) throws PlaceExistsException {
        User user = userRepository.getUserByEmail(username);
        Place place = placeRepository.getPlaceByName(placeDto.getName());
        if (place != null) {
            if (place.getUsers().isEmpty()) {
                throw new PlaceExistsException("Użytkownik nie może tworzyć miejsca, które jest predefiniowane");
            }
            if (place.getUsers().contains(user)) {
                throw new PlaceExistsException("Użytkownik już stworzył takie miejsce");
            }
            place.getUsers().add(user);
            placeRepository.updatePlace(place);
        } else {
            Set<User> users = new HashSet<>(Arrays.asList(user));
            placeRepository.createPlace(placeDto.getName(), users, 'F');
        }
    }

    public PlaceDto getPlaceDtoById(Integer id) {
        return PlaceDto.fromPlace(placeRepository.getPlaceById(id));
    }

    public List<TransactionDto> getUserPlaceTransactionsDtoByIdInRange(String username, Integer id, Integer start, Integer max) {
        User user = userRepository.getUserByEmail(username);
        Place place = placeRepository.getPlaceById(id);
        List<Transaction> transactions = (List<Transaction>) transactionRepository.getUserTransactionsAssignedToPlaceInRange(user, place, start, max);
        List<TransactionDto> transactionDtos = new ArrayList<>();
        if (!transactions.isEmpty()) transactions.forEach(transaction -> transactionDtos.add(TransactionDto.fromTransaction(transaction)));
        return transactionDtos;
    }

    @Transactional
    public void removeUserPlace(String username, Integer id) {
        User user = userRepository.getUserByEmail(username);
        Place place = placeRepository.getPlaceById(id);
        transactionRepository.removePlaceFromTransacitons(user, place);
        if (place.getUsers().size() == 1) {
            placeRepository.removePlace(id);
        } else {
            place.getUsers().remove(user);
            placeRepository.updatePlace(place);
        }
    }

    @Transactional
    public Integer changeUserPlaceName(String username, PlaceDto placeDto) throws PlaceExistsException {
        User user = userRepository.getUserByEmail(username);
        Place oldPlace = placeRepository.getPlaceById(placeDto.getId());
        Place newPlace = placeRepository.getPlaceByName(placeDto.getName());
        if (oldPlace.getUsers().size() == 1) {
            if (newPlace == null) {
                placeRepository.changeUserPlaceName(user, placeDto);
                return -1;
            } else if (newPlace.getUsers().contains(user)) {
                throw new PlaceExistsException("Nie możesz zmienić nazwę miejsca na nazwę już istniejącego!");
            } else {
                newPlace.getUsers().add(user);
                placeRepository.updatePlace(newPlace);
                transactionRepository.changePlaceInUserTransactions(user, oldPlace, newPlace);
                placeRepository.removePlace(oldPlace.getId());
                return newPlace.getId();
            }
        } else {
            if (newPlace == null) {
                placeRepository.createPlace(placeDto.getName(), new HashSet<>(Arrays.asList(user)), 'F');
                Place createdPlace = placeRepository.getPlaceByName(placeDto.getName());
                transactionRepository.changePlaceInUserTransactions(user, oldPlace, createdPlace);
                oldPlace.getUsers().remove(user);
                placeRepository.updatePlace(oldPlace);
                return createdPlace.getId();
            } else if (newPlace.getUsers().contains(user)) {
                throw new PlaceExistsException("Nie możesz zmienić nazwę miejsca na nazwę już istniejącego!");
            } else {
                newPlace.getUsers().add(user);
                placeRepository.updatePlace(newPlace);
                transactionRepository.changePlaceInUserTransactions(user, oldPlace, newPlace);
                oldPlace.getUsers().remove(user);
                placeRepository.updatePlace(oldPlace);
                return newPlace.getId();
            }
        }
    }
}
