package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.Place;
import pl.jakubpradzynski.crispus.domain.TransactionCategory;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.domain.UserType;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createUserType(String name, String surname, String email, String passwordHash, String salt, String phoneNumber, UserType userType, Set<Place> placeList, Set<TransactionCategory> transactionCategoryList) {
        User user = new User(email, passwordHash, salt, name, surname, phoneNumber, userType, placeList, transactionCategoryList);
        entityManager.persist(user);
    }

    @Transactional
    public void createUser(User user) {
        entityManager.persist(user);
    }

    public User getUserById(Integer id) {
        return entityManager.find(User.class, id);
    }

    public User getUserByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT u FROM USER u WHERE u.email=:email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Collection<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM USER u", User.class)
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
}
