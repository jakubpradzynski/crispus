package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.Place;
import pl.jakubpradzynski.crispus.domain.Category;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.domain.UserType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A repository-type class to perform database queries related to users.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createUser(String name, String surname, String email, String passwordHash, String salt, String phoneNumber, UserType userType, Set<Place> placeList, Set<Category> categoryList) {
        User user = new User(email, passwordHash, salt, name, surname, phoneNumber, userType, placeList, categoryList);
        entityManager.persist(user);
    }

    /**
     * Method creates new user in database.
     * @param user - user which will be saved
     */
    @Transactional
    public void createUser(User user) {
        entityManager.persist(user);
    }

    public User getUserById(Integer id) {
        return entityManager.find(User.class, id);
    }

    /**
     * Method returns user specific by email.
     * @param email - user email
     * @return User (when user was found and null otherwise)
     */
    public User getUserByEmail(String email) {
        List<User> users = entityManager.createQuery("SELECT u FROM USERS u WHERE u.email=:email", User.class)
                .setParameter("email", email)
                .getResultList();
        if (users.isEmpty()) return null;
        else  return  users.get(0);
    }

    public Collection<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM USERS u", User.class)
                .getResultList();
    }

    @Transactional
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Transactional
    public void deleteUser(Integer id) {
        entityManager.remove(id);
    }

    /**
     * Method changes user's type.
     * @param user - user which type we want to change
     * @param userType - new user type
     */
    @Transactional
    public void changeUserType(User user, UserType userType) {
        entityManager.createQuery("UPDATE USERS u SET u.userType=:userType WHERE u.id=:id")
                .setParameter("userType", userType)
                .setParameter("id", user.getId())
                .executeUpdate();
    }
}
