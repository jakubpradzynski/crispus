package pl.jakubpradzynski.crispus.domain;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "USER_TYPE")
@Table(name = "USER_TYPE")
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_type_id_generator")
    @SequenceGenerator(name="user_type_id_generator", sequenceName="user_type_seq_for_id", initialValue = 1, allocationSize = 1)
    @NotNull
    @Column(name = "ID")
    private Integer id;

    @NotNull(message = "Nazwa typu użytkownika nie może być pusta")
    @Size(max = 20, message = "Nazwa typu użytkownika nie może mieć więcej niż 20 znaków")
    @Column(name = "NAME", unique=true)
    private String name;

    @NotNull(message = "Limit kont nie może być pusty")
    @Range(min = 1, max = 5, message = "Limit kont musi być z zakresu od 1 do 5")
    @Column(name = "ACCOUNT_LIMIT")
    private Integer accountLimit;

    @NotNull(message = "Limit kategorii transakcji nie może być pusty")
    @Range(min = 1, max = 15, message = "Limit kategorii transakcji musi być z zakresu od 1 do 15")
    @Column(name = "CATEGORY_LIMIT")
    private Integer categoryLimit;

    @NotNull(message = "Limit miejsc nie może być pusty")
    @Range(min = 1, max = 15, message = "Limit miejsc musi być z zakresu od 1 do 15")
    @Column(name = "PLACE_LIMIT")
    private Integer placeLimit;

    public UserType() {
    }

    public UserType(@NotNull(message = "Nazwa typu użytkownika nie może być pusta") @Size(max = 20, message = "Nazwa typu użytkownika nie może mieć więcej niż 20 znaków") String name, @NotNull(message = "Limit kont nie może być pusty") @Range(min = 1, max = 5, message = "Limit kont musi być z zakresu od 1 do 5") Integer accountLimit, @NotNull(message = "Limit kategorii transakcji nie może być pusty") @Range(min = 1, max = 15, message = "Limit kategorii transakcji musi być z zakresu od 1 do 15") Integer categoryLimit, @NotNull(message = "Limit miejsc nie może być pusty") @Range(min = 1, max = 15, message = "Limit miejsc musi być z zakresu od 1 do 15") Integer placeLimit) {
        this.name = name;
        this.accountLimit = accountLimit;
        this.categoryLimit = categoryLimit;
        this.placeLimit = placeLimit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAccountLimit() {
        return accountLimit;
    }

    public void setAccountLimit(Integer accountLimit) {
        this.accountLimit = accountLimit;
    }

    public Integer getCategoryLimit() {
        return categoryLimit;
    }

    public void setCategoryLimit(Integer categoryLimit) {
        this.categoryLimit = categoryLimit;
    }

    public Integer getPlaceLimit() {
        return placeLimit;
    }

    public void setPlaceLimit(Integer placeLimit) {
        this.placeLimit = placeLimit;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accountLimit=" + accountLimit +
                ", categoryLimit=" + categoryLimit +
                ", placeLimit=" + placeLimit +
                '}';
    }
}
