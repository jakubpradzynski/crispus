package pl.jakubpradzynski.crispus.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserLoginDto {

    @NotNull
    @Email
    private String login;

    @NotNull
    private String password;

    public UserLoginDto() {
    }

    public UserLoginDto(@NotNull @Email String login, @NotNull String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserLoginDto{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
