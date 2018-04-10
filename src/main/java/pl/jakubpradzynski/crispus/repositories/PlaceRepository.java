package pl.jakubpradzynski.crispus.repositories;

import org.springframework.core.CollectionFactory;
import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.Place;
import pl.jakubpradzynski.crispus.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
        return entityManager.createQuery("SELECT p FROM PLACE p WHERE p.description=:description", Place.class)
                .setParameter("description", description)
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

    @Transactional
    public void updatePlace(Place place) {
        entityManager.merge(place);
    }

    @Transactional
    public void deletePlace(Integer id) {
        entityManager.remove(id);
    }
}
