package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;

@Repository
public class TransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

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

    @Transactional
    public void updateTransaction(Transaction transaction) {
        entityManager.merge(transaction);
    }

    @Transactional
    public void deleteTransaction(Integer id) {
        entityManager.remove(id);
    }
}
