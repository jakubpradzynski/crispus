package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.domain.UserType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;

/**
 * A repository-type class to perform database queries related to user types.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Repository
public class UserTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method creates new user type in database.
     * @param name - user type name
     * @param accountLimit - account limit in this user type
     * @param transactionCategoryLimit - category limit in this user type
     * @param placeLimit - place limit in this user type
     */
    @Transactional
    public void createUserType(String name, Integer accountLimit, Integer transactionCategoryLimit, Integer placeLimit) {
        UserType userType = new UserType(name, accountLimit, transactionCategoryLimit, placeLimit);
        entityManager.persist(userType);
    }

    @Transactional
    public void createUserType(UserType userType) {
        entityManager.persist(userType);
    }

    public UserType getUserTypeById(Integer id) {
        return entityManager.find(UserType.class, id);
    }

    /**
     * Method returns user type specific by name.
     * @param name - user type name
     * @return UserType
     */
    public UserType getUserTypeByName(String name) {
        return entityManager.createQuery("SELECT ut FROM USER_TYPE ut WHERE ut.name=:name", UserType.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    /**
     * Method returns account number available for specific user.
     * @param user - user which account number we want to receive
     * @return Integer (available account number)
     */
    public Integer getAccountNumberAvailableForUser(User user) {
        return entityManager.createQuery("SELECT ut.accountLimit FROM USER_TYPE ut WHERE ut.id=:id", Integer.class)
                .setParameter("id", user.getUserType().getId())
                .getSingleResult();
    }

    public Collection<UserType> getAllUserTypes() {
        return entityManager.createQuery("SELECT ut FROM USER_TYPE ut", UserType.class)
                .getResultList();
    }

    @Transactional
    public void updateUserType(UserType userType) {
        entityManager.merge(userType);
    }

    @Transactional
    public void deleteUserType(Integer id) {
        entityManager.remove(id);
    }

    /**
     * Method returns place number available for specific user.
     * @param user - user which place number we want to receive
     * @return Integer (available place number)
     */
    public Integer getPlaceNumberAvailableForUser(User user) {
        return entityManager.createQuery("SELECT ut.placeLimit FROM USER_TYPE ut WHERE ut.id=:id", Integer.class)
                .setParameter("id", user.getUserType().getId())
                .getSingleResult();
    }

    /**
     * Method returns category number available for specific user.
     * @param user - user which category number we want to receive
     * @return Integer (available category number)
     */
    public Integer getCategoryNumberAvailableForUser(User user) {
        return entityManager.createQuery("SELECT ut.categoryLimit FROM USER_TYPE ut WHERE ut.id=:id", Integer.class)
                .setParameter("id", user.getUserType().getId())
                .getSingleResult();
    }
}
