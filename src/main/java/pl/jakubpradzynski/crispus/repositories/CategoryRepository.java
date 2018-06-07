package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.Category;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.CategoryDto;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A repository-type class to perform database queries related to categories.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Repository
public class CategoryRepository extends RepositoryClass<Category> {

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) create in RepositoryClass}
     */
    @Override
    @Transactional
    public void create(Category category) {
        entityManager.persist(category);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) getById in RepositoryClass}
     */
    @Override
    public Category getById(Integer id) {
        return entityManager.find(Category.class, id);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) delete in RepositoryClass}
     */
    @Override
    @Transactional
    public void delete(Category category) {
        entityManager.remove(category);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) remove in RepositoryClass}
     */
    @Override
    @Transactional
    public void remove(Integer id) {
        entityManager.createQuery("DELETE FROM CATEGORY c WHERE c.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) update in RepositoryClass}
     */
    @Override
    @Transactional
    public void update(Category category) {
        entityManager.merge(category);
    }

    /**
     * Method create new category in database.
     * @param name - name of category
     * @param userSet - set of category's users
     * @param isPredefined - is predifined or not
     */
    @Transactional
    public void createTransactionCategory(String name, Set<User> userSet, Character isPredefined) {
        Category category = new Category(name, userSet, isPredefined);
        entityManager.persist(category);
    }

    /**
     * Method returns category specific by name.
     * @param name - category name
     * @return Category
     */
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

    /**
     * Method returns all categories names available for user.
     * @param user - user which categories names we want to receive
     * @return List of String (categories names)
     */
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

    /**
     * Method returns all predefined categories.
     * @return List of Category (predefined categories)
     */
    public Collection<Category> getAllPreDefinedCategories() {
        return entityManager.createQuery("SELECT tc FROM CATEGORY tc WHERE tc.isPredefined=:isPredefined", Category.class)
                .setParameter("isPredefined", 'T')
                .getResultList();
    }

    /**
     * Method returns number of user's used categories.
     * @param user - user which used categories number we want to receive
     * @return Integer (number of used categories)
     */
    public Integer getUserUsedCategoriesNumber(User user) {
        return entityManager.createQuery("SELECT COUNT(tc) FROM CATEGORY tc WHERE :user in elements(tc.users) AND tc.isPredefined=:isPredefined", Long.class)
                .setParameter("user", user)
                .setParameter("isPredefined", 'F')
                .getSingleResult().intValue();
    }

    /**
     * Method returns categories created by specific user.
     * @param user - user which created categories we want to receive
     * @return List of Category (created by user)
     */
    public Collection<Category> getCategoriesCreatedByUser(User user) {
        return entityManager.createQuery("SELECT tc FROM CATEGORY tc WHERE :user in elements(tc.users) AND tc.isPredefined=:isPredefined", Category.class)
                .setParameter("user", user)
                .setParameter("isPredefined", 'F')
                .getResultList();
    }

    /**
     * Method change user's category name.
     * @param user - user which category we want to change
     * @param categoryDto - new category data
     */
    @Transactional
    public void changeUserCategoryName(User user, CategoryDto categoryDto) {
        entityManager.createQuery("UPDATE CATEGORY tc SET tc.name=:name WHERE tc.id=:id AND :user in elements(tc.users)")
                .setParameter("name", categoryDto.getName())
                .setParameter("id", categoryDto.getId())
                .setParameter("user", user)
                .executeUpdate();
    }
}
