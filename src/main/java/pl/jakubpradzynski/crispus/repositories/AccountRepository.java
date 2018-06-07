package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.*;
import pl.jakubpradzynski.crispus.dto.ChangeAccountNameDto;

import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * A repository-type class to perform database queries related to accounts.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Repository
public class AccountRepository extends RepositoryClass<Account> {

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) create in RepositoryClass}
     */
    @Override
    @Transactional
    public void create(Account account) {
        entityManager.persist(account);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) getById in RepositoryClass}
     */
    @Override
    public Account getById(Integer id) {
        return entityManager.find(Account.class, id);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) delete in RepositoryClass}
     */
    @Override
    @Transactional
    public void delete(Account account) {
        entityManager.remove(account);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) remove in RepositoryClass}
     */
    @Override
    @Transactional
    public void remove(Integer id) {
        entityManager.createQuery("DELETE FROM ACCOUNT a WHERE a.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) update in RepositoryClass}
     */
    @Override
    @Transactional
    public void update(Account account) {
        entityManager.merge(account);
    }

    /**
     * Method create new user account in database.
     * @param user - user which create new account
     * @param name - name of new account
     * @param moneyAmount - starting money amount on new account
     */
    @Transactional
    public void create(User user, String name, Double moneyAmount) {
        Account account = new Account(user, name, moneyAmount);
        entityManager.persist(account);
    }

    @Transactional
    public void createAccount(User user, String name, Double moneyAmount) {
        Account account = new Account(user, name, moneyAmount);
        entityManager.persist(account);
    }

    /**
     * Method retrieves a user account from a database after a name.
     * @param user - user which account we want to receive
     * @param name - name of this account
     * @return Account
     */
    public Account getUserAccountByName(User user, String name) {
        List<Account> accountList = entityManager.createQuery("SELECT a FROM ACCOUNT a WHERE a.user=:user AND a.name=:name", Account.class)
                .setParameter("user", user)
                .setParameter("name", name)
                .getResultList();
        if (accountList.isEmpty()) return null;
        return accountList.get(0);
    }

    /**
     * Method retrieves all user accounts from the database from the database.
     * @param user - user which all accounts we want to receive
     * @return List of Account
     */
    public Collection<Account> getAllUserAccounts(User user) {
        return entityManager.createQuery("SELECT a FROM ACCOUNT a WHERE a.user=:user", Account.class)
                .setParameter("user", user)
                .getResultList();
    }

    /**
     *
     * @param user - user which all accounts names we want to receive
     * @return List of String (accounts names)
     */
    public Collection<String> getAllUserAccountsNames(User user) {
        return entityManager.createQuery("SELECT a.name FROM ACCOUNT a WHERE a.user=:user", String.class)
                .setParameter("user", user)
                .getResultList();
    }

    /**
     * Method returns information on how many accounts the user has already used.
     * @param user - user which number of used accounts we want to receive
     * @return Integer (number of used accounts)
     */
    public Integer getUserUsedAccountsNumber(User user) {
        return entityManager.createQuery("SELECT COUNT(a) FROM ACCOUNT a WHERE a.user=:user", Long.class)
                .setParameter("user", user)
                .getSingleResult().intValue();
    }

    /**
     * Method returns the id of the account found after the name and user.
     * @param user - user which account id we wean to receive
     * @param accountName - account name which id we wean to receive
     * @return Integer (account id)
     */
    public Integer getUserAccountIdByName(User user, String accountName) {
        return entityManager.createQuery("SELECT a.id FROM ACCOUNT a WHERE a.user=:user AND a.name=:name")
                .setParameter("user", user)
                .setParameter("name", accountName)
                .getFirstResult();
    }

    /**
     * Method updates the account balance after the transaction.
     * @param account - account which will be update
     * @param value - value of transaction
     */
    @Transactional
    public void updateAccountAfterTransaction(Account account, @javax.validation.constraints.NotNull Double value) {
        entityManager.createQuery("UPDATE ACCOUNT a SET a.moneyAmount=:val WHERE a.id=:id")
                .setParameter("val", value + account.getMoneyAmount())
                .setParameter("id", account.getId())
                .executeUpdate();
    }

    /**
     * Method change user's account data.
     * @param user - user which account we want to change
     * @param changeAccountNameDto - new data of account
     */
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
