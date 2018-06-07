package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.*;
import pl.jakubpradzynski.crispus.dto.MonthlyBudgetInfoDto;

import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * A repository-type class to perform database queries related to monthly budgets.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Repository
public class MonthlyBudgetRepository extends RepositoryClass<MonthlyBudget> {

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) create in RepositoryClass}
     */
    @Override
    @Transactional
    public void create(MonthlyBudget monthlyBudget) {
        entityManager.persist(monthlyBudget);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) getById in RepositoryClass}
     */
    @Override
    public MonthlyBudget getById(Integer id) {
        return entityManager.find(MonthlyBudget.class, id);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) delete in RepositoryClass}
     */
    @Override
    @Transactional
    public void delete(MonthlyBudget monthlyBudget) {
        entityManager.remove(monthlyBudget);
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) remove in RepositoryClass}
     */
    @Override
    @Transactional
    public void remove(Integer id) {
        entityManager.createQuery("DELETE FROM MONTHLY_BUDGET mb WHERE mb.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    /**
     * {@link pl.jakubpradzynski.crispus.repositories.RepositoryClass#create(Object) update in RepositoryClass}
     */
    @Override
    @Transactional
    public void update(MonthlyBudget monthlyBudget) {
        entityManager.merge(monthlyBudget);
    }

    /**
     * Methods create new budget in database.
     * @param user - user which create new monthly budget
     * @param startDate - start date of budget
     * @param endDate - end date of budget
     * @param amount - budget amount
     */
    @Transactional
    public void createMonthlyBudget(User user, Date startDate, Date endDate, Double amount) {
        MonthlyBudget monthlyBudget = new MonthlyBudget(user, startDate, endDate, amount);
        entityManager.persist(monthlyBudget);
    }

    public MonthlyBudget getUserMonthlyBudgetByDate(User user, Date date) {
        return entityManager.createQuery("SELECT mb FROM MONTHLY_BUDGET mb WHERE mb.user=:user AND :date BETWEEN mb.startDate AND mb.endDate", MonthlyBudget.class)
                .setParameter("user", user)
                .setParameter("date", date)
                .getSingleResult();
    }

    /**
     * Method returns all monthly budgets created by user.
     * @param user - user which all monthly budgets we want to receive
     * @return List of MonthlyBudget
     */
    public Collection<MonthlyBudget> getAllUserMonthlyBudgets(User user) {
        return entityManager.createQuery("SELECT mb FROM MONTHLY_BUDGET mb WHERE mb.user=:user", MonthlyBudget.class)
                .setParameter("user", user)
                .getResultList();
    }

    /**
     * Method returns information about actual user's monthly budget.
     * @param user - user which actual monthly budget we want to receive
     * @return MonthlyBudgetInfoDto (info about actual user's monthly budget)
     */
    public MonthlyBudgetInfoDto getUserActualMonthlyBudgetDto(User user) {
        Date date = new Date();
        List<MonthlyBudget> monthlyBudgets = entityManager.createQuery("SELECT mb FROM MONTHLY_BUDGET mb WHERE mb.user=:user AND :date BETWEEN mb.startDate AND mb.endDate", MonthlyBudget.class)
                .setParameter("user", user)
                .setParameter("date", date)
                .getResultList();
        if (monthlyBudgets.isEmpty()) return null;
        return MonthlyBudgetInfoDto.fromMonthlyBudget(monthlyBudgets.get(0));
    }
}
