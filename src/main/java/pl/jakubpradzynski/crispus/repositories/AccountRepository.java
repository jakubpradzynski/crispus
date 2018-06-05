package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.*;
import pl.jakubpradzynski.crispus.dto.ChangeAccountNameDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
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

    public Integer getUserAccountIdByName(User user, String accountName) {
        return entityManager.createQuery("SELECT a.id FROM ACCOUNT a WHERE a.user=:user AND a.name=:name")
                .setParameter("user", user)
                .setParameter("name", accountName)
                .getFirstResult();
    }

    @Transactional
    public void updateAccountAfterTransaction(Account account, @javax.validation.constraints.NotNull Double value) {
        entityManager.createQuery("UPDATE ACCOUNT a SET a.moneyAmount=:val WHERE a.id=:id")
                .setParameter("val", value + account.getMoneyAmount())
                .setParameter("id", account.getId())
                .executeUpdate();
    }

    @Transactional
    public void updateAccount(User user) {
        entityManager.merge(user);
    }

    @Transactional
    public void deleteAccount(Integer id) {
        entityManager.remove(id);
    }

    @Transactional
    public void deleteAccount(Account account) {
        entityManager.remove(account);
    }

    @Transactional
    public void removeAccount(Integer id) {
        entityManager.createQuery("DELETE FROM ACCOUNT a WHERE a.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Transactional
    public void changeUserAccountByName(User user, ChangeAccountNameDto changeAccountNameDto) {
        entityManager.createQuery("UPDATE ACCOUNT a SET a.name=:newName WHERE a.name=:oldName AND a.user=:user")
                .setParameter("newName", changeAccountNameDto.getNewName())
                .setParameter("oldName", changeAccountNameDto.getOldName())
                .setParameter("user", user)
                .executeUpdate();
    }

    @Transactional
    public void removeUserAccountByName(User user, String name) {
        entityManager.createQuery("DELETE FROM ACCOUNT a WHERE a.user=:user AND a.name=:name")
                .setParameter("user", user)
                .setParameter("name", name)
                .executeUpdate();
    }
}
