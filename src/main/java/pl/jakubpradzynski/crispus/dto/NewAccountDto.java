package pl.jakubpradzynski.crispus.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewAccountDto {

    @NotNull
    private String username;

    @NotNull(message = "Nazwa konta nie może być pusta")
    @Size(min = 3, max = 30, message = "Nazwa konta musi mieć od 3 do 30 znaków")
    private String name;

    @NotNull(message = "Kwota na koncie nie może być pusta")
    private Double moneyAmount;

    public NewAccountDto() {
    }

    public NewAccountDto(@NotNull String username, @NotNull(message = "Nazwa konta nie może być pusta") @Size(min = 3, max = 30, message = "Nazwa konta musi mieć od 3 do 30 znaków") String name, @NotNull(message = "Kwota na koncie nie może być pusta") Double moneyAmount) {
        this.username = username;
        this.name = name;
        this.moneyAmount = moneyAmount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        return "NewAccountDto{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}
