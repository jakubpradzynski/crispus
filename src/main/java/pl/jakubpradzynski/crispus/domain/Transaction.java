package pl.jakubpradzynski.crispus.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity(name = "TRANSACTION")
@Table(name = "TRANSACTION")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "ID")
    private Integer id;

    @NotNull
    @Size(min = 1, max = 250, message = "Opis transakcji może mieć maksymalnie 250 znaków")
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USER_FK")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_FK")
    private Account account;

    @NotNull
    @Column(name = "VALUE")
    private Double value;

    @NotNull
    @Column(name = "DATE")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "PLACE_FK")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "TRANSACTION_CATEGORY_FK")
    private TransactionCategory transactionCategory;

    public Transaction() {
    }

    public Transaction(@Size(max = 250, message = "Opis transakcji może mieć maksymalnie 250 znaków") String description, @NotNull User user, @NotNull Account account, @NotNull Double value, @NotNull Date date, Place place, TransactionCategory transactionCategory) {
        this.description = description;
        this.user = user;
        this.account = account;
        this.value = value;
        this.date = date;
        this.place = place;
        this.transactionCategory = transactionCategory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public TransactionCategory getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(TransactionCategory transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", user=" + user +
                ", account=" + account +
                ", value=" + value +
                ", date=" + date +
                ", place.html=" + place +
                ", transactionCategory=" + transactionCategory +
                '}';
    }
}
