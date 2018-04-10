package pl.jakubpradzynski.crispus.domain;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "MONTHLY_BUDGET")
@Table(name = "MONTHLY_BUDGET")
public class MonthlyBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "ID")
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USER_FK")
    private User user;

    @NotNull(message = "Miesiąc nie może być pusty")
    @Size(min = 3, max = 20, message = "Miesiąc musi mieć od 3 do 20 znaków")
    @Column(name = "MONTH")
    private String month;

    @NotNull(message = "Rok nie może być pusty")
    @Range(min = 2000, max = 2050, message = "Rok musi być z zakresu 2000-2050")
    @Column(name = "YEAR")
    private Integer year;

    @NotNull
    @Column(name = "AMOUNT")
    private Double amount;

    public MonthlyBudget() {
    }

    public MonthlyBudget(@NotNull User user, @NotNull(message = "Miesiąc nie może być pusty") @Size(min = 3, max = 20, message = "Miesiąc musi mieć od 3 do 20 znaków") String month, @NotNull(message = "Rok nie może być pusty") @Range(min = 2000, max = 2050, message = "Rok musi być z zakresu 2000-2050") Integer year, @NotNull Double amount) {
        this.user = user;
        this.month = month;
        this.year = year;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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
                ", month='" + month + '\'' +
                ", year=" + year +
                ", amount=" + amount +
                '}';
    }
}
