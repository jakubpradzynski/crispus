package pl.jakubpradzynski.crispus.repositories;

import org.springframework.core.CollectionFactory;
import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.Place;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.PlaceDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PlaceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createPlace(String description, Set<User> userSet) {
        Place place = new Place(description, userSet);
        entityManager.persist(place);
    }

    @Transactional
    public void createPlace(Place place) {
        entityManager.persist(place);
    }

    public Place getPlaceById(Integer id) {
        return entityManager.find(Place.class, id);
    }

    public Place getPlaceByDescription(String description) {
        List<Place> places = entityManager.createQuery("SELECT p FROM PLACE p WHERE p.description=:description", Place.class)
                .setParameter("description", description)
                .getResultList();
        if (places.isEmpty()) {
            return null;
        }
        return places.get(0);
    }

    public Place getPlaceAvailableForUserByDescription(User user, String description) {
        Collection<Place> places = getAllPreDefinedPlaces();
        List<Place> filterPlaces =  places.stream().filter(place -> place.getDescription().equals(description)).collect(Collectors.toList());
        if (!filterPlaces.isEmpty()) return filterPlaces.get(0);
        return entityManager.createQuery("SELECT p FROM PLACE p WHERE p.description=:description AND p.user=:user", Place.class)
                .setParameter("description", description)
                .setParameter("user", user)
                .getSingleResult();

    }

    public Collection<Place> getAllPlaces() {
        return entityManager.createQuery("SELECT p FROM PLACE p", Place.class)
                .getResultList();
    }

    public Collection<Place> getAllPreDefinedPlaces() {
        return entityManager.createQuery("SELECT p FROM PLACE p", Place.class)
                .getResultList().stream().filter(place -> place.getUsers().isEmpty()).collect(Collectors.toSet());
    }

    public Collection<String> getAllPlacesDescriptionsAvailableForUser(User user) {
        Collection<Place> preDefinedPlaces = getAllPreDefinedPlaces();
        Collection<String> allPlacesDescriptions = new ArrayList<>();
        preDefinedPlaces.forEach(place -> allPlacesDescriptions.add(place.getDescription()));

        Collection<Place> userDefinedPlaces = entityManager.createQuery("SELECT p FROM PLACE p WHERE :user in elements(p.users)", Place.class)
                .setParameter("user", user)
                .getResultList();

        userDefinedPlaces.forEach(place -> allPlacesDescriptions.add(place.getDescription()));

        return allPlacesDescriptions;
    }

    public Integer getUserUsedPlacesNumber(User user) {
        return entityManager.createQuery("SELECT COUNT(p) FROM PLACE p WHERE :user in elements(p.users) ", Long.class)
                .setParameter("user", user)
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
        return entityManager.createQuery("SELECT p FROM PLACE p WHERE :user in elements(p.users)", Place.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Transactional
    public void removePlace(Integer id) {
        entityManager.createQuery("DELETE FROM PLACE p WHERE p.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Transactional
    public void changeUserPlaceDescription(User user, PlaceDto placeDto) {
        entityManager.createQuery("UPDATE PLACE p SET p.description=:description WHERE p.id=:id AND :user in elements(p.users)")
                .setParameter("description", placeDto.getDescription())
                .setParameter("id", placeDto.getId())
                .setParameter("user", user)
                .executeUpdate();
    }
}
