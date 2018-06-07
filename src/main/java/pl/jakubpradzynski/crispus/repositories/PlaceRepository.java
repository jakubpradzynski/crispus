package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.Place;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.PlaceDto;

import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A repository-type class to perform database queries related to places.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Repository
public class PlaceRepository extends RepositoryClass<Place> {

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) create in RepositoryClass}
     */
    @Override
    @Transactional
    public void create(Place place) {
        entityManager.persist(place);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) getById in RepositoryClass}
     */
    @Override
    public Place getById(Integer id) {
        return entityManager.find(Place.class, id);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) delete in RepositoryClass}
     */
    @Override
    @Transactional
    public void delete(Place place) {
        entityManager.remove(place);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) remove in RepositoryClass}
     */
    @Override
    @Transactional
    public void remove(Integer id) {
        entityManager.createQuery("DELETE FROM PLACE p WHERE p.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) update in RepositoryClass}
     */
    @Override
    @Transactional
    public void update(Place place) {
        entityManager.merge(place);
    }

    /**
     * Method create new place in database.
     * @param name - new place name
     * @param userSet - place users set
     * @param isPredefined - is predefined or not
     */
    @Transactional
    public void createPlace(String name, Set<User> userSet, Character isPredefined) {
        Place place = new Place(name, userSet, isPredefined);
        entityManager.persist(place);
    }

    /**
     * Methods returns place specific by name.
     * @param name - place name
     * @return Place
     */
    public Place getPlaceByName(String name) {
        List<Place> places = entityManager.createQuery("SELECT p FROM PLACE p WHERE p.name=:name", Place.class)
                .setParameter("name", name)
                .getResultList();
        if (places.isEmpty()) {
            return null;
        }
        return places.get(0);
    }

    public Place getPlaceAvailableForUserByName(User user, String name) {
        Collection<Place> places = getAllPreDefinedPlaces();
        List<Place> filterPlaces =  places.stream().filter(place -> place.getName().equals(name)).collect(Collectors.toList());
        if (!filterPlaces.isEmpty()) return filterPlaces.get(0);
        return entityManager.createQuery("SELECT p FROM PLACE p WHERE p.name=:name AND p.user=:user", Place.class)
                .setParameter("name", name)
                .setParameter("user", user)
                .getSingleResult();

    }

    public Collection<Place> getAllPlaces() {
        return entityManager.createQuery("SELECT p FROM PLACE p", Place.class)
                .getResultList();
    }

    /**
     * Method returns all predefined places.
     * @return List of Place (predefined places)
     */
    public Collection<Place> getAllPreDefinedPlaces() {
        return entityManager.createQuery("SELECT p FROM PLACE p WHERE p.isPredefined=:isPredefined", Place.class)
                .setParameter("isPredefined", 'T')
                .getResultList();
    }

    /**
     * Method returns all places names available to choose by user.
     * @param user - user which available places names we want to receive
     * @return List of String (places names)
     */
    public Collection<String> getAllPlacesNamesAvailableForUser(User user) {
        Collection<Place> preDefinedPlaces = getAllPreDefinedPlaces();
        Collection<String> allPlacesNames = new ArrayList<>();
        preDefinedPlaces.forEach(place -> allPlacesNames.add(place.getName()));

        Collection<Place> userDefinedPlaces = entityManager.createQuery("SELECT p FROM PLACE p WHERE :user in elements(p.users) AND p.isPredefined=:isPredefined", Place.class)
                .setParameter("user", user)
                .setParameter("isPredefined", 'F')
                .getResultList();

        userDefinedPlaces.forEach(place -> allPlacesNames.add(place.getName()));

        return allPlacesNames;
    }

    /**
     * Method returns number of user's used places.
     * @param user - user which used places number we want to receive
     * @return Integer (number of used places)
     */
    public Integer getUserUsedPlacesNumber(User user) {
        return entityManager.createQuery("SELECT COUNT(p) FROM PLACE p WHERE :user in elements(p.users) AND p.isPredefined=:isPredefined", Long.class)
                .setParameter("user", user)
                .setParameter("isPredefined", 'F')
                .getSingleResult().intValue();
    }

    /**
     * Method returns places created by user.
     * @param user - user which places we want to receive
     * @return List of Place (places created by user)
     */
    public Collection<Place> getPlaceCreatedByUser(User user) {
        return entityManager.createQuery("SELECT p FROM PLACE p WHERE :user in elements(p.users) AND p.isPredefined=:isPredefined", Place.class)
                .setParameter("user", user)
                .setParameter("isPredefined", 'F')
                .getResultList();
    }

    /**
     * Method changes user's place name.
     * @param user - user which place name we want to change
     * @param placeDto - new place data
     */
    @Transactional
    public void changeUserPlaceName(User user, PlaceDto placeDto) {
        entityManager.createQuery("UPDATE PLACE p SET p.name=:name WHERE p.id=:id AND :user in elements(p.users)")
                .setParameter("name", placeDto.getName())
                .setParameter("id", placeDto.getId())
                .setParameter("user", user)
                .executeUpdate();
    }
}
