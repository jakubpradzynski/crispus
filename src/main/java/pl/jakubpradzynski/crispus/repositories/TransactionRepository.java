package pl.jakubpradzynski.crispus.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.*;
import pl.jakubpradzynski.crispus.dto.MonthlyBudgetInfoDto;
import pl.jakubpradzynski.crispus.dto.TransactionDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public void createTransaction(String description, User user, Account account, Double value, Date date, Place place, Category category) {
        Transaction transaction = new Transaction(description, user, account, value, date, place, category);
        accountRepository.updateAccountAfterTransaction(account, value);
        System.out.println("Jestem tu");
        System.out.println(transaction.getId());
        entityManager.persist(transaction);
    }

    @Transactional
    public void createTransaction(Transaction transaction) {
        accountRepository.updateAccountAfterTransaction(transaction.getAccount(), transaction.getValue());
        entityManager.persist(transaction);
    }

    public Transaction getTransactionById(Integer id) {
        return entityManager.find(Transaction.class, id);
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

    public Double getTransactionExpensesAmountForAccount(Account account) {
        return entityManager.createQuery("SELECT SUM(t.value) FROM TRANSACTION t WHERE t.account=:account AND t.value < 0", Double.class)
                .setParameter("account", account)
                .getSingleResult();
    }

    public Double getTransactionRevenuesAmountForAccount(Account account) {
        return entityManager.createQuery("SELECT SUM(t.value) FROM TRANSACTION t WHERE t.account=:account AND t.value >= 0", Double.class)
                .setParameter("account", account)
                .getSingleResult();
    }

    @Transactional
    public void updateTransaction(Transaction transaction) {
        entityManager.merge(transaction);
    }

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

    @Transactional
    public void deleteTransaction(Integer id) {
        entityManager.remove(id);
    }

    @Transactional
    public void removeTransaction(Integer id) {
        entityManager.createQuery("DELETE FROM TRANSACTION t WHERE t.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public Collection<Transaction> getAllTransactionsAssignedToAccount(Account account) {
        return entityManager.createQuery("SELECT t FROM TRANSACTION t WHERE t.account=:account", Transaction.class)
                .setParameter("account", account)
                .getResultList();
    }

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

    public Collection<Transaction> getUserTransactionsAssignedToPlaceInRange(User user, Place place, Integer start, Integer max) {
        return entityManager.createQuery("SELECT t FROM TRANSACTION t WHERE t.place=:place AND t.user=:user ORDER BY t.date DESC", Transaction.class)
                .setParameter("place", place)
                .setParameter("user", user)
                .setFirstResult(start)
                .setMaxResults(max)
                .getResultList();
    }

    @Transactional
    public void removePlaceFromTransacitons(User user, Place place) {
        entityManager.createQuery("UPDATE TRANSACTION t SET t.place=null WHERE t.place=:place AND t.user=:user")
                .setParameter("place", place)
                .setParameter("user", user)
                .executeUpdate();
    }

    @Transactional
    public void changePlaceInUserTransactions(User user, Place oldPlace, Place newPlace) {
        entityManager.createQuery("UPDATE TRANSACTION t SET t.place=:newPlace WHERE t.place=:oldPlace AND t.user=:user")
                .setParameter("newPlace", newPlace)
                .setParameter("oldPlace", oldPlace)
                .setParameter("user", user)
                .executeUpdate();
    }

    public Collection<Transaction> getUserTransactionsAssignedToCategoryInRange(User user, Category category, Integer start, Integer max) {
        return entityManager.createQuery("SELECT t FROM TRANSACTION t WHERE t.category=:category AND t.user=:user ORDER BY t.date DESC", Transaction.class)
                .setParameter("category", category)
                .setParameter("user", user)
                .setFirstResult(start)
                .setMaxResults(max)
                .getResultList();
    }

    @Transactional
    public void removeCategoryFromTransacitons(User user, Category category) {
        entityManager.createQuery("UPDATE TRANSACTION t SET t.category=null WHERE t.category=:category AND t.user=:user")
                .setParameter("category", category)
                .setParameter("user", user)
                .executeUpdate();
    }

    @Transactional
    public void changeCategoryInUserTransactions(User user, Category oldCategory, Category newCategory) {
        entityManager.createQuery("UPDATE TRANSACTION t SET t.category=:newCategory WHERE t.category=:oldCategory AND t.user=:user")
                .setParameter("newCategory", newCategory)
                .setParameter("oldCategory", oldCategory)
                .setParameter("user", user)
                .executeUpdate();
    }

    public Double getUserBudgetUsedAmount(User user, MonthlyBudgetInfoDto monthlyBudgetInfoDto) {
        List<Transaction> budgetTransactions = (List<Transaction>) getUserTransactionsInBudget(user, monthlyBudgetInfoDto);
        final Double[] usedAmount = {0.};
        budgetTransactions.forEach(transaction -> {
             usedAmount[0] += transaction.getValue();
        });
        return usedAmount[0];
    }

    public Collection<Transaction> getUserTransactionsInBudget(User user, MonthlyBudgetInfoDto monthlyBudgetInfoDto) {
        List<Transaction> budgetTransactions = entityManager.createQuery("SELECT t FROM TRANSACTION t WHERE t.user=:user AND t.date BETWEEN :startDate AND :endDate", Transaction.class)
                .setParameter("user", user)
                .setParameter("startDate", monthlyBudgetInfoDto.getStartDate())
                .setParameter("endDate", monthlyBudgetInfoDto.getEndDate())
                .getResultList();
        return budgetTransactions.stream().filter(transaction -> transaction.getValue() < 0).collect(Collectors.toList());
    }
}
