package pl.jakubpradzynski.crispus.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * A domain class that maps the user's transaction.
 * It contains: id, description, user who created it, account from which it was paid, value, date and optional place and category.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Entity(name = "TRANSACTION")
@Table(name = "TRANSACTION")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_generator")
    @SequenceGenerator(name="transaction_id_generator", sequenceName="transaction_seq_for_id", initialValue = 1, allocationSize = 1)
    @NotNull
    @Column(name = "ID")
    private Integer id;

    @NotNull
    @Size(min = 1, max = 250, message = "Opis transakcji może mieć maksymalnie 250 znaków")
    @Column(name = "T_DESCRIPTION")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USERS_ID")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @NotNull
    @Column(name = "T_VALUE", columnDefinition = "NUMBER")
    private Double value;

    @NotNull
    @Column(name = "T_DATE")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "PLACE_ID")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    public Transaction() {
    }

    public Transaction(@Size(max = 250, message = "Opis transakcji może mieć maksymalnie 250 znaków") String description, @NotNull User user, @NotNull Account account, @NotNull Double value, @NotNull Date date, Place place, Category category) {
        this.description = description;
        this.user = user;
        this.account = account;
        this.value = value;
        this.date = date;
        this.place = place;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
                ", place=" + place +
                ", category=" + category +
                '}';
    }
}
