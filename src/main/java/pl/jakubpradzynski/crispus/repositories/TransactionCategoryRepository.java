package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.TransactionCategory;
import pl.jakubpradzynski.crispus.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TransactionCategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createTransactionCategory(String name, Set<User> userSet) {
        TransactionCategory transactionCategory = new TransactionCategory(name, userSet);
        entityManager.persist(transactionCategory);
    }

    @Transactional
    public void createTransactionCategory(TransactionCategory transactionCategory) {
        entityManager.persist(transactionCategory);
    }

    public TransactionCategory getTransactionCategoryById(Integer id) {
        return entityManager.find(TransactionCategory.class, id);
    }

    public TransactionCategory getTransactionCategoryByName(String name) {
        return entityManager.createQuery("SELECT tc FROM TRANSACTION_CATEGORY tc WHERE tc.name=:name", TransactionCategory.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    public Collection<TransactionCategory> getAllTransactionCategories() {
        return entityManager.createQuery("SELECT tc FROM TRANSACTION_CATEGORY tc ", TransactionCategory.class)
                .getResultList();
    }

    public Collection<TransactionCategory> getAllPreDefinedTransactionCategories() {
        return entityManager.createQuery("SELECT tc FROM TRANSACTION_CATEGORY tc", TransactionCategory.class)
                .getResultList().stream().filter(transactionCategory -> transactionCategory.getUsers().isEmpty()).collect(Collectors.toSet());
    }

    @Transactional
    public void updateTransactionCategory(TransactionCategory transactionCategory) {
        entityManager.merge(transactionCategory);
    }

    @Transactional
    public void deleteTransactionCategory(Integer id) {
        entityManager.remove(id);
    }
}
