package pl.jakubpradzynski.crispus.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity(name = "MONTHLY_BUDGET")
@Table(name = "MONTHLY_BUDGET")
public class MonthlyBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "monthly_budget_id_generator")
    @SequenceGenerator(name="monthly_budget_id_generator", sequenceName="monthly_budget_seq_for_id", initialValue = 1, allocationSize = 1)
    @NotNull
    @Column(name = "ID")
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USERS_ID")
    private User user;

    @NotNull(message = "Data początkowa nie może być pusta")
    @Column(name = "START_DATE")
    private Date startDate;

    @NotNull(message = "Data końcowa nie może być pusta")
    @Column(name = "END_DATE")
    private Date endDate;

    @NotNull
    @Column(name = "AMOUNT", columnDefinition = "NUMBER")
    private Double amount;

    public MonthlyBudget() {
    }

    public MonthlyBudget(@NotNull User user, @NotNull(message = "Data początkowa nie może być pusta") Date startDate, @NotNull(message = "Data końcowa nie może być pusta") Date endDate, @NotNull Double amount) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "MonthlyBudget{" +
                "id=" + id +
                ", user=" + user +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", amount=" + amount +
                '}';
    }
}
