package pl.jakubpradzynski.crispus.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.*;
import pl.jakubpradzynski.crispus.dto.TransactionDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
    private TransactionCategoryRepository transactionCategoryRepository;

    @Transactional
    public void createTransaction(String description, User user, Account account, Double value, Date date, Place place, TransactionCategory transactionCategory) {
        Transaction transaction = new Transaction(description, user, account, value, date, place, transactionCategory);
        entityManager.persist(transaction);
    }

    @Transactional
    public void createTransaction(Transaction transaction) {
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

    @Transactional
    public void updateTransaction(Transaction transaction) {
        entityManager.merge(transaction);
    }

    @Transactional
    public void updateTransaction(Integer id, TransactionDto transactionDto) throws ParseException {
        User user = userRepository.getUserByEmail(transactionDto.getUsername());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("jestem tu 3");
        entityManager.createQuery("UPDATE TRANSACTION t SET t.description=:description, t.account=:account, t.value=:val, t.date=:date, t.place=:place, t.transactionCategory=:category WHERE t.id=:id")
                .setParameter("description", transactionDto.getDescription())
                .setParameter("account", accountRepository.getUserAccountByName(user, transactionDto.getAccountName()))
                .setParameter("val", transactionDto.getValue())
                .setParameter("date", format.parse(transactionDto.getDate()))
                .setParameter("place", !transactionDto.getPlaceDescription().equals("") ? placeRepository.getPlaceByDescription(transactionDto.getPlaceDescription()) : null)
                .setParameter("category", !transactionDto.getTransactionCategoryName().equals("") ? transactionCategoryRepository.getTransactionCategoryByName(transactionDto.getTransactionCategoryName()) : null)
                .setParameter("id", id)
                .executeUpdate();
        System.out.println("jestem tu 7");
    }

    @Transactional
    public void deleteTransaction(Integer id) {
        entityManager.remove(id);
    }
}
