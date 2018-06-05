package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.Place;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.PlaceDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PlaceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createPlace(String name, Set<User> userSet, Character isPredefined) {
        Place place = new Place(name, userSet, isPredefined);
        entityManager.persist(place);
    }

    @Transactional
    public void createPlace(Place place) {
        entityManager.persist(place);
    }

    public Place getPlaceById(Integer id) {
        return entityManager.find(Place.class, id);
    }

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

    public Collection<Place> getAllPreDefinedPlaces() {
        return entityManager.createQuery("SELECT p FROM PLACE p WHERE p.isPredefined=:isPredefined", Place.class)
                .setParameter("isPredefined", 'T')
                .getResultList();
    }

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

    public Integer getUserUsedPlacesNumber(User user) {
        return entityManager.createQuery("SELECT COUNT(p) FROM PLACE p WHERE :user in elements(p.users) AND p.isPredefined=:isPredefined", Long.class)
                .setParameter("user", user)
                .setParameter("isPredefined", 'F')
                .getSingleResult().intValue();
    }

    @Transactional
    public void updatePlace(Place place) {
        entityManager.merge(place);
    }

    @Transactional
    public void deletePlace(Integer id) {
        entityManager.remove(id);
    }

    public Collection<Place> getPlaceCreatedByUser(User user) {
        return entityManager.createQuery("SELECT p FROM PLACE p WHERE :user in elements(p.users) AND p.isPredefined=:isPredefined", Place.class)
                .setParameter("user", user)
                .setParameter("isPredefined", 'F')
                .getResultList();
    }

    @Transactional
    public void removePlace(Integer id) {
        entityManager.createQuery("DELETE FROM PLACE p WHERE p.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Transactional
    public void changeUserPlaceName(User user, PlaceDto placeDto) {
        entityManager.createQuery("UPDATE PLACE p SET p.name=:name WHERE p.id=:id AND :user in elements(p.users)")
                .setParameter("name", placeDto.getName())
                .setParameter("id", placeDto.getId())
                .setParameter("user", user)
                .executeUpdate();
    }
}
