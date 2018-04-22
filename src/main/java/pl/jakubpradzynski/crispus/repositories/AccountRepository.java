package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;

@Repository
public class AccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createAccount(User user, String name, Double moneyAmount) {
        Account account = new Account(user, name, moneyAmount);
        entityManager.persist(account);
    }

    @Transactional
    public void createAccount(Account account) {
        entityManager.persist(account);
    }

    public Account getAccountById(Integer id) {
        return entityManager.find(Account.class, id);
    }

    public Account getUserAccountByName(User user, String name) {
        System.out.println("jestem tu 4");
        return entityManager.createQuery("SELECT a FROM ACCOUNT a WHERE a.user=:user AND a.name=:name", Account.class)
                .setParameter("user", user)
                .setParameter("name", name)
                .getSingleResult();
    }

    public Collection<Account> getAllUserAccounts(User user) {
        return entityManager.createQuery("SELECT a FROM ACCOUNT a WHERE a.user=:user", Account.class)
                .setParameter("user", user)
                .getResultList();
    }

    public Collection<String> getAllUserAccountsNames(User user) {
        return entityManager.createQuery("SELECT a.name FROM ACCOUNT a WHERE a.user=:user", String.class)
                .setParameter("user", user)
                .getResultList();
    }

    public Integer getUserUsedAccountsNumber(User user) {
        return entityManager.createQuery("SELECT COUNT(a) FROM ACCOUNT a WHERE a.user=:user", Long.class)
                .setParameter("user", user)
                .getSingleResult().intValue();
    }

    @Transactional
    public void updateAccount(User user) {
        entityManager.merge(user);
    }

    @Transactional
    public void deleteAccount(Integer id) {
        entityManager.remove(id);
    }
}
