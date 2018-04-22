package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.Place;
import pl.jakubpradzynski.crispus.domain.TransactionCategory;
import pl.jakubpradzynski.crispus.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
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


    public TransactionCategory getUserTransactionCategoryByName(User user, String name) {
        return entityManager.createQuery("SELECT tc FROM TRANSACTION_CATEGORY tc WHERE tc.name=:name AND user=:user", TransactionCategory.class)
                .setParameter("name", name)
                .setParameter("user", user)
                .getSingleResult();
    }

    public TransactionCategory getTransactionCategoryAvailableForUserByName(User user, String name) {
        List<TransactionCategory> transactionCategories = (List<TransactionCategory>) getAllPreDefinedTransactionCategories();
        List<TransactionCategory> filterTransactionCategories = transactionCategories.stream().filter(transactionCategory -> transactionCategory.getName().equals(name)).collect(Collectors.toList());

        if (!filterTransactionCategories.isEmpty()) return filterTransactionCategories.get(0);
        System.out.println("jestem tu 6");
        return entityManager.createQuery("SELECT tc FROM TRANSACTION_CATEGORY tc WHERE tc.name=:name AND user=:user", TransactionCategory.class)
                .setParameter("name", name)
                .setParameter("user", user)
                .getSingleResult();
    }

    public Collection<TransactionCategory> getAllTransactionCategories() {
        return entityManager.createQuery("SELECT tc FROM TRANSACTION_CATEGORY tc ", TransactionCategory.class)
                .getResultList();
    }

    public Collection<String> getAllTransactionCategoriesNamesAvailableForUser(User user) {
        Collection<TransactionCategory> preDefinedTransactionCategories = getAllPreDefinedTransactionCategories();
        Collection<String> allTransactionCategoriesNames = new ArrayList<>();
        preDefinedTransactionCategories.forEach(transactionCategory -> allTransactionCategoriesNames.add(transactionCategory.getName()));

        Collection<TransactionCategory> userDefinedTransactionCategories = entityManager.createQuery("SELECT tc FROM TRANSACTION_CATEGORY tc WHERE :user in elements(tc.users)", TransactionCategory.class)
                .setParameter("user", user)
                .getResultList();

        userDefinedTransactionCategories.forEach(transactionCategory -> allTransactionCategoriesNames.add(transactionCategory.getName()));

        return allTransactionCategoriesNames;
    }

    public Collection<TransactionCategory> getAllPreDefinedTransactionCategories() {
        return entityManager.createQuery("SELECT tc FROM TRANSACTION_CATEGORY tc", TransactionCategory.class)
                .getResultList().stream().filter(transactionCategory -> transactionCategory.getUsers().isEmpty()).collect(Collectors.toSet());
    }

    public Integer getUserUsedTransactionCategoriesNumber(User user) {
        return entityManager.createQuery("SELECT COUNT(tc) FROM TRANSACTION_CATEGORY tc WHERE :user in elements(tc.users) ", Long.class)
                .setParameter("user", user)
                .getSingleResult().intValue();
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
