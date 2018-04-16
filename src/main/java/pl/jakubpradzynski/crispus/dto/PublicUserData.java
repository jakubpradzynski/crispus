package pl.jakubpradzynski.crispus.dto;

import pl.jakubpradzynski.crispus.domain.UserType;
import pl.jakubpradzynski.crispus.validators.phone.number.PhoneCase;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PublicUserData {

    @NotNull(message = "Imie nie może być puste")
    @Size(min = 3, max = 20, message = "Imie musi mieć od 3 do 20 znaków")
    private String name;

    @NotNull(message = "Nazwisko nie może być puste")
    @Size(min = 3, max = 30, message = "Nazwisko musi mieć od 3 do 30 znaków")
    private String surname;

    @NotNull
    @Email
    @Size(max = 50, message = "EmailValidator musi mieć maksymalnie 50 znaków")
    private String email;

    @PhoneCase
    private String phoneNumber;

    @NotNull
    private UserType userType;

    @NotNull
    private Integer usedAccounts;

    @NotNull
    private Integer usedPlaces;

    @NotNull
    private Integer usedTransactionCategories;

    public PublicUserData() {}

    public PublicUserData(@NotNull(message = "Imie nie może być puste") @Size(min = 3, max = 20, message = "Imie musi mieć od 3 do 20 znaków") String name, @NotNull(message = "Nazwisko nie może być puste") @Size(min = 3, max = 30, message = "Nazwisko musi mieć od 3 do 30 znaków") String surname, @NotNull @Email @Size(max = 50, message = "EmailValidator musi mieć maksymalnie 50 znaków") String email, String phoneNumber, @NotNull UserType userType, @NotNull Integer usedAccounts, @NotNull Integer usedPlaces, @NotNull Integer usedTransactionCategories) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.usedAccounts = usedAccounts;
        this.usedPlaces = usedPlaces;
        this.usedTransactionCategories = usedTransactionCategories;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getUsedAccounts() {
        return usedAccounts;
    }

    public void setUsedAccounts(Integer usedAccounts) {
        this.usedAccounts = usedAccounts;
    }

    public Integer getUsedPlaces() {
        return usedPlaces;
    }

    public void setUsedPlaces(Integer usedPlaces) {
        this.usedPlaces = usedPlaces;
    }

    public Integer getUsedTransactionCategories() {
        return usedTransactionCategories;
    }

    public void setUsedTransactionCategories(Integer usedTransactionCategories) {
        this.usedTransactionCategories = usedTransactionCategories;
    }

    @Override
    public String toString() {
        return "PublicUserData{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userType=" + userType +
                ", usedAccounts=" + usedAccounts +
                ", usedPlaces=" + usedPlaces +
                ", usedTransactionCategories=" + usedTransactionCategories +
                '}';
    }
}
