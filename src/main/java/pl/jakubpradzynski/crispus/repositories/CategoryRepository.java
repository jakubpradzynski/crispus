package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.Category;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.CategoryDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createTransactionCategory(String name, Set<User> userSet, Character isPredefined) {
        Category category = new Category(name, userSet, isPredefined);
        entityManager.persist(category);
    }

    @Transactional
    public void createCategory(Category category) {
        entityManager.persist(category);
    }

    public Category getCategoryById(Integer id) {
        return entityManager.find(Category.class, id);
    }

    public Category getCategoryByName(String name) {
        List<Category> transactionCategories = entityManager.createQuery("SELECT tc FROM CATEGORY tc WHERE tc.name=:name", Category.class)
                .setParameter("name", name)
                .getResultList();
        if (transactionCategories.isEmpty()) {
            return null;
        }
        return transactionCategories.get(0);
    }


    public Category getUserCategoryByName(User user, String name) {
        return entityManager.createQuery("SELECT tc FROM CATEGORY tc WHERE tc.name=:name AND user=:user", Category.class)
                .setParameter("name", name)
                .setParameter("user", user)
                .getSingleResult();
    }

    public Category getCategoryAvailableForUserByName(User user, String name) {
        List<Category> transactionCategories = (List<Category>) getAllPreDefinedCategories();
        List<Category> filterTransactionCategories = transactionCategories.stream().filter(transactionCategory -> transactionCategory.getName().equals(name)).collect(Collectors.toList());

        if (!filterTransactionCategories.isEmpty()) return filterTransactionCategories.get(0);
        return entityManager.createQuery("SELECT tc FROM CATEGORY tc WHERE tc.name=:name AND user=:user", Category.class)
                .setParameter("name", name)
                .setParameter("user", user)
                .getSingleResult();
    }

    public Collection<Category> getAllCategories() {
        return entityManager.createQuery("SELECT tc FROM CATEGORY tc ", Category.class)
                .getResultList();
    }

    public Collection<String> getAllCategoriesNamesAvailableForUser(User user) {
        Collection<Category> preDefinedTransactionCategories = getAllPreDefinedCategories();
        Collection<String> allTransactionCategoriesNames = new ArrayList<>();
        preDefinedTransactionCategories.forEach(transactionCategory -> allTransactionCategoriesNames.add(transactionCategory.getName()));

        Collection<Category> userDefinedTransactionCategories = entityManager.createQuery("SELECT tc FROM CATEGORY tc WHERE :user in elements(tc.users) AND tc.isPredefined=:isPredefined", Category.class)
                .setParameter("user", user)
                .setParameter("isPredefined", 'F')
                .getResultList();

        userDefinedTransactionCategories.forEach(transactionCategory -> allTransactionCategoriesNames.add(transactionCategory.getName()));

        return allTransactionCategoriesNames;
    }

    public Collection<Category> getAllPreDefinedCategories() {
        return entityManager.createQuery("SELECT tc FROM CATEGORY tc WHERE tc.isPredefined=:isPredefined", Category.class)
                .setParameter("isPredefined", 'T')
                .getResultList();
    }

    public Integer getUserUsedCategoriesNumber(User user) {
        return entityManager.createQuery("SELECT COUNT(tc) FROM CATEGORY tc WHERE :user in elements(tc.users) AND tc.isPredefined=:isPredefined", Long.class)
                .setParameter("user", user)
                .setParameter("isPredefined", 'F')
                .getSingleResult().intValue();
    }

    @Transactional
    public void updateCategory(Category category) {
        entityManager.merge(category);
    }

    @Transactional
    public void deleteTransactionCategory(Integer id) {
        entityManager.remove(id);
    }

    public Collection<Category> getCategoriesCreatedByUser(User user) {
        return entityManager.createQuery("SELECT tc FROM CATEGORY tc WHERE :user in elements(tc.users) AND tc.isPredefined=:isPredefined", Category.class)
                .setParameter("user", user)
                .setParameter("isPredefined", 'F')
                .getResultList();
    }

    @Transactional
    public void removeCategory(Integer id) {
        entityManager.createQuery("DELETE FROM CATEGORY tc WHERE tc.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Transactional
    public void changeUserCategoryName(User user, CategoryDto categoryDto) {
        entityManager.createQuery("UPDATE CATEGORY tc SET tc.name=:name WHERE tc.id=:id AND :user in elements(tc.users)")
                .setParameter("name", categoryDto.getName())
                .setParameter("id", categoryDto.getId())
                .setParameter("user", user)
                .executeUpdate();
    }
}
