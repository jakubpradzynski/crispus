package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.UserType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;

@Repository
public class UserTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

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

    public UserType getUserTypeByName(String name) {
        return entityManager.createQuery("SELECT ut FROM USER_TYPE ut WHERE ut.name=:name", UserType.class)
                .setParameter("name", name)
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
}
