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

import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * A service-type class related to places.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Returns number of used places by user after calling Place Repository for this data.
     * @param username - user's email
     * @return Integer (user used places number)
     */
    public Integer getUserUsedPlacesNumber(String username) {
        User user = userRepository.getUserByEmail(username);
        return placeRepository.getUserUsedPlacesNumber(user);
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Returns number of available places for user after calling Place Repository for this data.
     * @param username - user's email
     * @return Integer (user available place number)
     */
    public Integer getUserAvailablePlacesNumber(String username) {
        User user = userRepository.getUserByEmail(username);
        return userTypeRepository.getPlaceNumberAvailableForUser(user);
    }

    /**
     * Method returns all predefined places info after calling Place Repository for this data.
     * @return List of PlaceDto
     */
    public List<PlaceDto> getPreDefinedPlaces() {
        Collection<Place> allPreDefinedPlaces = placeRepository.getAllPreDefinedPlaces();
        List<PlaceDto> preDefinedPlacesDto = new ArrayList<>();
        if (allPreDefinedPlaces.size() > 0) allPreDefinedPlaces.forEach(place -> preDefinedPlacesDto.add(PlaceDto.fromPlace(place)));
        return preDefinedPlacesDto;
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Returns places created by user after calling Place Repository for this data.
     * @param username - user's email
     * @return List of PlaceDto
     */
    public List<PlaceDto> getUserCreatedPlaces(String username) {
        User user = userRepository.getUserByEmail(username);
        Collection<Place> userCreatedPlaces = placeRepository.getPlaceCreatedByUser(user);
        List<PlaceDto> userCreatePlacesDto = new ArrayList<>();
        userCreatedPlaces.forEach(place -> userCreatePlacesDto.add(PlaceDto.fromPlace(place)));
        return userCreatePlacesDto;
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Creates new user place by call a function from Place Repository and validate if category has already exist.
     * @param username - user's email
     * @param placeDto - new place data
     * @throws PlaceExistsException - Exception thrown after trying create place which already exists.
     */
    @Transactional
    public void addNewUserPlace(String username, PlaceDto placeDto) throws PlaceExistsException {
        User user = userRepository.getUserByEmail(username);
        Place place = placeRepository.getPlaceByName(placeDto.getName());
        if (place != null) {
            if (place.getPredefined().equals('T')) {
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

    /**
     * Method returns info about place specific by id.
     * @param id - place if
     * @return PlaceDto
     */
    public PlaceDto getPlaceDtoById(Integer id) {
        return PlaceDto.fromPlace(placeRepository.getPlaceById(id));
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Returns list of transactions info assigned to place in specific range.
     * @param username - user's email
     * @param id - place id
     * @param start - start of range
     * @param max - max number in range
     * @return List of TransactionDto
     */
    public List<TransactionDto> getUserPlaceTransactionsDtoByIdInRange(String username, Integer id, Integer start, Integer max) {
        User user = userRepository.getUserByEmail(username);
        Place place = placeRepository.getPlaceById(id);
        List<Transaction> transactions = (List<Transaction>) transactionRepository.getUserTransactionsAssignedToPlaceInRange(user, place, start, max);
        List<TransactionDto> transactionDtos = new ArrayList<>();
        if (!transactions.isEmpty()) transactions.forEach(transaction -> transactionDtos.add(TransactionDto.fromTransaction(transaction)));
        return transactionDtos;
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Removes user place if place has assigned only one user or remove user from set of users in place.
     * @param username - user's email
     * @param id - place id
     */
    @Transactional
    public void removeUserPlace(String username, Integer id) {
        User user = userRepository.getUserByEmail(username);
        Place place = placeRepository.getPlaceById(id);
        transactionRepository.removePlaceFromTransactions(user, place);
        if (place.getUsers().size() == 1) {
            placeRepository.removePlace(id);
        } else {
            place.getUsers().remove(user);
            placeRepository.updatePlace(place);
        }
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Change user's place name if new name hasn't already exist and place has only one assigned user or create new place with new name,
     * swap transactions assigned to old place to new place,
     * remove user from set of users in old place.
     * @param username - user's email
     * @param placeDto - new data of place
     * @return Integer (place id or -1)
     * @throws PlaceExistsException - Exception thrown after trying create place which already exists.
     */
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
