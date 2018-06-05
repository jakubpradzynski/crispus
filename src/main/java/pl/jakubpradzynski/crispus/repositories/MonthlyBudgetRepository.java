package pl.jakubpradzynski.crispus.repositories;

import org.springframework.stereotype.Repository;
import pl.jakubpradzynski.crispus.domain.*;
import pl.jakubpradzynski.crispus.dto.MonthlyBudgetInfoDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Repository
public class MonthlyBudgetRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createMonthlyBudget(User user, Date startDate, Date endDate, Double amount) {
        MonthlyBudget monthlyBudget = new MonthlyBudget(user, startDate, endDate, amount);
        entityManager.persist(monthlyBudget);
    }

    @Transactional
    public void createMonthlyBudget(MonthlyBudget monthlyBudget) {
        entityManager.persist(monthlyBudget);
    }

    public MonthlyBudget getMonthlyBudgetById(Integer id) {
        return entityManager.find(MonthlyBudget.class, id);
    }

    public MonthlyBudget getUserMonthlyBudgetByDate(User user, Date date) {
        return entityManager.createQuery("SELECT mb FROM MONTHLY_BUDGET mb WHERE mb.user=:user AND :date BETWEEN mb.startDate AND mb.endDate", MonthlyBudget.class)
                .setParameter("user", user)
                .setParameter("date", date)
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
