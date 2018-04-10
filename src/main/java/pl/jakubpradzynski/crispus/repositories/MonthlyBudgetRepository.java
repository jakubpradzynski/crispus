package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;

@Repository
public class MonthlyBudgetRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createMonthlyBudget(User user, String month, Integer year, Double amount) {
        MonthlyBudget monthlyBudget = new MonthlyBudget(user, month, year, amount);
        entityManager.persist(monthlyBudget);
    }

    @Transactional
    public void createMonthlyBudget(MonthlyBudget monthlyBudget) {
        entityManager.persist(monthlyBudget);
    }

    public MonthlyBudget getMonthlyBudgetById(Integer id) {
        return entityManager.find(MonthlyBudget.class, id);
    }

    public MonthlyBudget getUserMonthlyBudgetByDate(User user, String month, Integer year) {
        return entityManager.createQuery("SELECT mb FROM MONTHLY_BUDGET mb WHERE mb.user=:user AND mb.month=:month AND mb.year=:year", MonthlyBudget.class)
                .setParameter("user", user)
                .setParameter("month", month)
                .setParameter("year", year)
                .getSingleResult();
    }

    public Collection<MonthlyBudget> getAllUserMonthlyBudgets(User user) {
        return entityManager.createQuery("SELECT mb FROM MONTHLY_BUDGET mb WHERE mb.user=:user", MonthlyBudget.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Transactional
    public void updateMonthlyBudget(MonthlyBudget monthlyBudget) {
        entityManager.merge(monthlyBudget);
    }

    @Transactional
    public void deleteMonthlyBudget(Integer id) {
        entityManager.remove(id);
    }
}
