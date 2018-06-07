package pl.jakubpradzynski.crispus.repositories;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Base class for the repository type classes.
 * @param <T> - type of object to be managed by the repository
 */
public abstract class RepositoryClass<T> {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Method creates T type object in database.
     * @param t - object to be create in database
     */
    @Transactional
    public abstract void create(T t);

    /**
     * Method returns T type object specific by id from database.
     * @param id - id of object which we want to receive from database
     * @return - T type object
     */
    public abstract T getById(Integer id);

    /**
     * Method deletes T type object from database.
     * @param t - object to be delete from database
     */
    @Transactional
    public abstract void delete(T t);

    /**
     * Method removes T type object specific by id from database.
     * @param id - id of object which we want to remove from database
     */
    @Transactional
    public abstract void remove(Integer id);

    /**
     * Method updates T type object in database.
     * @param t - object to be update in database
     */
    @Transactional
    public abstract void update(T t);


}
