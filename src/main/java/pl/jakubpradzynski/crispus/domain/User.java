package pl.jakubpradzynski.crispus.domain;

import pl.jakubpradzynski.crispus.validators.phone.number.PhoneCase;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity(name = "USERS")
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_generator")
    @SequenceGenerator(name="users_id_generator", sequenceName="users_seq_for_id", initialValue = 1, allocationSize = 1)
    @Column(name = "ID")
    @NotNull
    private Integer id;

    @NotNull
    @Email
    @Size(max = 50, message = "Email musi mieć maksymalnie 50 znaków")
    @Column(name = "EMAIL", unique=true)
    private String email;

    @NotNull
    @Column(name = "PASSWORD_HASH")
    private String passwordHash;

    @NotNull
    @Column(name = "SALT")
    private String salt;

    @NotNull(message = "Imie nie może być puste")
    @Size(min = 3, max = 20, message = "Imie musi mieć od 3 do 20 znaków")
    @Column(name = "NAME")
    private String name;

    @NotNull(message = "Nazwisko nie może być puste")
    @Size(min = 3, max = 30, message = "Nazwisko musi mieć od 3 do 30 znaków")
    @Column(name = "SURNAME")
    private String surname;

    @PhoneCase
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USER_TYPE_ID")
    private UserType userType;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_place",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "place_id") })
    private Set<Place> places;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_category",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") })
    private Set<Category> transactionCategories;

    public User() {
        super();
    }

    public User(@NotNull @Email @Size(max = 50, message = "EmailValidator musi mieć maksymalnie 50 znaków") String email, @NotNull String passwordHash, @NotNull String salt, @NotNull(message = "Imie nie może być puste") @Size(min = 3, max = 20, message = "Imie musi mieć od 3 do 20 znaków") String name, @NotNull(message = "Nazwisko nie może być puste") @Size(min = 3, max = 30, message = "Nazwisko musi mieć od 3 do 30 znaków") String surname, String phoneNumber, @NotNull UserType userType, Set<Place> places, Set<Category> transactionCategories) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.places = places;
        this.transactionCategories = transactionCategories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Set<Place> getPlaces() {
        return places;
    }

    public void setPlaces(Set<Place> places) {
        this.places = places;
    }

    public Set<Category> getTransactionCategories() {
        return transactionCategories;
    }

    public void setTransactionCategories(Set<Category> transactionCategories) {
        this.transactionCategories = transactionCategories;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", salt='" + salt + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userType=" + userType +
//                ", places=" + places +
//                ", transactionCategories=" + transactionCategories +
                '}';
    }
}
