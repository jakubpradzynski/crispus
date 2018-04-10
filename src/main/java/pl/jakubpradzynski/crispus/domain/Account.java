package pl.jakubpradzynski.crispus.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "ACCOUNT")
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @NotNull
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USER_FK")
    private User user;

    @NotNull(message = "Nazwa konta nie może być pusta")
    @Size(min = 3, max = 30, message = "Nazwa konta musi mieć od 3 do 30 znaków")
    @Column(name = "NAME")
    private String name;

    @NotNull(message = "Kwota na koncie nie może być pusta")
    @Column(name = "MONEY_AMOUNT")
    private Double moneyAmount;

    public Account() {
    }

    public Account(@NotNull User user, @NotNull(message = "Nazwa konta nie może być pusta") @Size(min = 3, max = 30, message = "Nazwa konta musi mieć od 3 do 30 znaków") String name, @NotNull(message = "Kwota na koncie nie może być pusta") Double moneyAmount) {
        this.user = user;
        this.name = name;
        this.moneyAmount = moneyAmount;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}
