package pl.jakubpradzynski.crispus.dto;

import pl.jakubpradzynski.crispus.validators.password.matches.MatchesPassword;
import pl.jakubpradzynski.crispus.validators.phone.number.PhoneCase;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@MatchesPassword
public class UserDto {

    @NotNull(message = "Imie nie może być puste")
    @Size(min = 3, max = 20, message = "Imie musi mieć od 3 do 20 znaków")
    private String name;

    @NotNull(message = "Nazwisko nie może być puste")
    @Size(min = 3, max = 30, message = "Nazwisko musi mieć od 3 do 30 znaków")
    private String surname;

    @NotNull
    private String password;

    @NotNull
    private String matchingPassword;

    @NotNull
    @Email
    @Size(max = 50, message = "EmailValidator musi mieć maksymalnie 50 znaków")
    private String email;

    @PhoneCase
    private String phoneNumber;

    public UserDto() {}

    public UserDto(@NotNull(message = "Imie nie może być puste") @Size(min = 3, max = 20, message = "Imie musi mieć od 3 do 20 znaków") String name, @NotNull(message = "Nazwisko nie może być puste") @Size(min = 3, max = 30, message = "Nazwisko musi mieć od 3 do 30 znaków") String surname, @NotNull String password, @NotNull String matchingPassword, @NotNull @Email @Size(max = 50, message = "EmailValidator musi mieć maksymalnie 50 znaków") String email, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    private UserDto(UserDtoBuilder builder) {
        this.name = builder.name;
        this.surname = builder.surname;
        this.password = builder.password;
        this.matchingPassword = builder.matchingPassword;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static class UserDtoBuilder {
        private final String name;
        private final String surname;
        private String password;
        private String matchingPassword;
        private String email;
        private String phoneNumber;

        public UserDtoBuilder(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }

        public UserDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserDtoBuilder matchingPassword(String matchingPassword) {
            this.matchingPassword = matchingPassword;
            return this;
        }

        public UserDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserDtoBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserDto build() {
            return new UserDto(this);
        }
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", matchingPassword='" + matchingPassword + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
