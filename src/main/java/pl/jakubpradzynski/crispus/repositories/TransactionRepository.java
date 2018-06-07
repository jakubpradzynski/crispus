package pl.jakubpradzynski.crispus.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.*;
import pl.jakubpradzynski.crispus.dto.MonthlyBudgetInfoDto;
import pl.jakubpradzynski.crispus.dto.TransactionDto;

import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A repository-type class to perform database queries related to transactions.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Repository
public class TransactionRepository extends RepositoryClass<Transaction> {

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) create in RepositoryClass}
     */
    @Override
    @Transactional
    public void create(Transaction transaction) {
        entityManager.persist(transaction);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) getById in RepositoryClass}
     */
    @Override
    public Transaction getById(Integer id) {
        return entityManager.find(Transaction.class, id);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) delete in RepositoryClass}
     */
    @Override
    @Transactional
    public void delete(Transaction transaction) {
        entityManager.remove(transaction);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) remove in RepositoryClass}
     */
    @Override
    @Transactional
    public void remove(Integer id) {
        entityManager.createQuery("DELETE FROM TRANSACTION t WHERE t.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) update in RepositoryClass}
     */
    @Override
    @Transactional
    public void update(Transaction transaction) {
        entityManager.merge(transaction);
    }

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Method creates new transaction in database.
     * @param description - transaction description
     * @param user - user which create transaction
     * @param account - transaction account
     * @param value - value of transaction
     * @param date - date of transaction
     * @param place - transaction place
     * @param category - transaction category
     */
    @Transactional
    public void createTransaction(String description, User user, Account account, Double value, Date date, Place place, Category category) {
        Transaction transaction = new Transaction(description, user, account, value, date, place, category);
        accountRepository.updateAccountAfterTransaction(account, value);
        entityManager.persist(transaction);
    }

    public Transaction getUserTransactionByDate(User user, Date date) {
        return entityManager.createQuery("SELECT t FROM TRANSACTION t WHERE t.user=:user AND t.date=:date", Transaction.class)
                .setParameter("user", user)
                .setParameter("date", date)
                .getSingleResult();
    }

    public Collection<Transaction> getAllUserTransactions(User user) {
        return entityManager.createQuery("SELECT t FROM TRANSACTION t WHERE t.user=:user", Transaction.class)
                .setParameter("user", user)
                .getResultList();
    }

    /**
     * Method returns info about last ten user's transactions.
     * @param user - user which last ten transactions we want to receive
     * @return List of TransactionDto
     */
    public Collection<TransactionDto> getLastTenUserTransactionsDto(User user) {
        List<Transaction> lastTenUserTransactions = entityManager.createQuery("SELECT t FROM TRANSACTION t WHERE t.user=:user ORDER BY t.date DESC", Transaction.class)
                .setParameter("user", user)
                .setMaxResults(10)
                .getResultList();
        List<TransactionDto> lastTenUserTransactionsDto = new ArrayList<>();
        for (Transaction transaction : lastTenUserTransactions)
            lastTenUserTransactionsDto.add(TransactionDto.fromTransaction(transaction));
        return lastTenUserTransactionsDto;
    }

    /**
     * Method returns transaction expanses amount for specific account.
     * @param account - account which expanses we want to receive
     * @return Double (expanses amount)
     */
    public Double getTransactionExpensesAmountForAccount(Account account) {
        return entityManager.createQuery("SELECT SUM(t.value) FROM TRANSACTION t WHERE t.account=:account AND t.value < 0", Double.class)
                .setParameter("account", account)
                .getSingleResult();
    }

    /**
     * Method returns transaction revenues amount for specific account.
     * @param account - account which revenues we want to receive
     * @return Double (revenues amount)
     */
    public Double getTransactionRevenuesAmountForAccount(Account account) {
        return entityManager.createQuery("SELECT SUM(t.value) FROM TRANSACTION t WHERE t.account=:account AND t.value >= 0", Double.class)
                .setParameter("account", account)
                .getSingleResult();
    }

    /**
     * Method updates transaction specific by id in database.
     * @param id - transaction id
     * @param transactionDto - new transaction's data
     * @throws ParseException - Exception is thrown when it is impossible to parse the date from the string.
     */
    @Transactional
    public void updateTransaction(Integer id, TransactionDto transactionDto) throws ParseException {
        User user = userRepository.getUserByEmail(transactionDto.getUsername());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        entityManager.createQuery("UPDATE TRANSACTION t SET t.description=:description, t.account=:account, t.value=:val, t.date=:date, t.place=:place, t.category=:category WHERE t.id=:id")
                .setParameter("description", transactionDto.getDescription())
                .setParameter("account", accountRepository.getUserAccountByName(user, transactionDto.getAccountName()))
                .setParameter("val", transactionDto.getValue())
                .setParameter("date", format.parse(transactionDto.getDate()))
                .setParameter("place", !transactionDto.getPlaceName().equals("") ? placeRepository.getPlaceByName(transactionDto.getPlaceName()) : null)
                .setParameter("category", !transactionDto.getTransactionCategoryName().equals("") ? categoryRepository.getCategoryByName(transactionDto.getTransactionCategoryName()) : null)
                .setParameter("id", id)
                .executeUpdate();
    }

    /**
     * Method returns all transactions assigned to specific account.
     * @param account - account which transactions we want to receive
     * @return List of Transaction
     */
    public Collection<Transaction> getAllTransactionsAssignedToAccount(Account account) {
        return entityManager.createQuery("SELECT t FROM TRANSACTION t WHERE t.account=:account", Transaction.class)
                .setParameter("account", account)
                .getResultList();
    }

    /**
     * Method returns info about user's transactions in specific range.
     * @param user - user which transactions we want to receive
     * @param start - start of range
     * @param max - end of range
     * @return List of TransactionDto
     */
    public List<TransactionDto> getUserTransactionByRange(User user, Integer start, Integer max) {
        List<Transaction> transactions = entityManager.createQuery("SELECT t FROM TRANSACTION t WHERE t.user=:user ORDER BY t.date DESC", Transaction.class)
                .setParameter("user", user)
                .setFirstResult(start)
                .setMaxResults(max)
                .getResultList();
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactions.forEach(transaction -> transactionDtoList.add(TransactionDto.fromTransaction(transaction)));
        return transactionDtoList;
    }

    /**
     * Method returns user's transactions assigned to specific place in range.
     * @param user - user which transactions we want to receive
     * @param place - place which transactions we want to receive
     * @param start - start of range
     * @param max - end of range
     * @return List of Transaction
     */
    public Collection<Transaction> getUserTransactionsAssignedToPlaceInRange(User user, Place place, Integer start, Integer max) {
        return entityManager.createQuery("SELECT t FROM TRANSACTION t WHERE t.place=:place AND t.user=:user ORDER BY t.date DESC", Transaction.class)
                .setParameter("place", place)
                .setParameter("user", user)
                .setFirstResult(start)
                .setMaxResults(max)
                .getResultList();
    }

    /**
     * Method removes place from transactions.
     * @param user - user which transactions we want to edit
     * @param place - place which we want to remove
     */
    @Transactional
    public void removePlaceFromTransactions(User user, Place place) {
        entityManager.createQuery("UPDATE TRANSACTION t SET t.place=null WHERE t.place=:place AND t.user=:user")
                .setParameter("place", place)
                .setParameter("user", user)
                .executeUpdate();
    }

    /**
     * Method changes place in user's transactions.
     * @param user - user which transactions we want to edit
     * @param oldPlace - place which we want to change
     * @param newPlace - new place
     */
    @Transactional
    public void changePlaceInUserTransactions(User user, Place oldPlace, Place newPlace) {
        entityManager.createQuery("UPDATE TRANSACTION t SET t.place=:newPlace WHERE t.place=:oldPlace AND t.user=:user")
                .setParameter("newPlace", newPlace)
                .setParameter("oldPlace", oldPlace)
                .setParameter("user", user)
                .executeUpdate();
    }

    /**
     * Method returns user's transactions assigned to specific category in range.
     * @param user - user which transactions we want to receive
     * @param category - category which transactions we want to receive
     * @param start - start of range
     * @param max - end of range
     * @return List of Transaction
     */
    public Collection<Transaction> getUserTransactionsAssignedToCategoryInRange(User user, Category category, Integer start, Integer max) {
        return entityManager.createQuery("SELECT t FROM TRANSACTION t WHERE t.category=:category AND t.user=:user ORDER BY t.date DESC", Transaction.class)
                .setParameter("category", category)
                .setParameter("user", user)
                .setFirstResult(start)
                .setMaxResults(max)
                .getResultList();
    }

    /**
     * Method removes category from transactions.
     * @param user - user which transactions we want to edit
     * @param category - category which we want to remove
     */
    @Transactional
    public void removeCategoryFromTransacitons(User user, Category category) {
        entityManager.createQuery("UPDATE TRANSACTION t SET t.category=null WHERE t.category=:category AND t.user=:user")
                .setParameter("category", category)
                .setParameter("user", user)
                .executeUpdate();
    }

    /**
     * Method changes category in user's transactions.
     * @param user - user which transactions we want to edit
     * @param oldCategory - category which we want to change
     * @param newCategory - new category
     */
    @Transactional
    public void changeCategoryInUserTransactions(User user, Category oldCategory, Category newCategory) {
        entityManager.createQuery("UPDATE TRANSACTION t SET t.category=:newCategory WHERE t.category=:oldCategory AND t.user=:user")
                .setParameter("newCategory", newCategory)
                .setParameter("oldCategory", oldCategory)
                .setParameter("user", user)
                .executeUpdate();
    }

    /**
     * Method returns amount used from user's monthly budget.
     * @param user - user which budget used amount we want to receive
     * @param monthlyBudgetInfoDto - information about monthly budget
     * @return Double (budget's used amount)
     */
    public Double getUserBudgetUsedAmount(User user, MonthlyBudgetInfoDto monthlyBudgetInfoDto) {
        List<Transaction> budgetTransactions = (List<Transaction>) getUserTransactionsInBudget(user, monthlyBudgetInfoDto);
        final Double[] usedAmount = {0.};
        budgetTransactions.forEach(transaction -> {
             usedAmount[0] += transaction.getValue();
        });
        return usedAmount[0];
    }

    /**
     * Method returns transactions in monthly budget.
     * @param user - user which transactions we want to receive
     * @param monthlyBudgetInfoDto - information about monthly budget
     * @return List of Transaction
     */
    public Collection<Transaction> getUserTransactionsInBudget(User user, MonthlyBudgetInfoDto monthlyBudgetInfoDto) {
        List<Transaction> budgetTransactions = entityManager.createQuery("SELECT t FROM TRANSACTION t WHERE t.user=:user AND t.date BETWEEN :startDate AND :endDate", Transaction.class)
                .setParameter("user", user)
                .setParameter("startDate", monthlyBudgetInfoDto.getStartDate())
                .setParameter("endDate", monthlyBudgetInfoDto.getEndDate())
                .getResultList();
        return budgetTransactions.stream().filter(transaction -> transaction.getValue() < 0).collect(Collectors.toList());
    }
}
