package pl.jakubpradzynski.crispus.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class NewMonthlyBudgetDto {

    private String username;

    @NotNull(message = "Data początkowa nie może być pusta")
    private String startDate = LocalDate.now().withDayOfMonth(1).toString();

    @NotNull(message = "Data końcowa nie może być pusta")
    private String endDate = LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1).toString();

    @NotNull(message = "Kwota nie może być pusta")
    @Min(value = 1, message = "Kwota musi być dodatnia")
    private Double amount;

    public NewMonthlyBudgetDto() {
    }

    public NewMonthlyBudgetDto(String username, @NotNull(message = "Data początkowa nie może być pusta") String startDate, @NotNull(message = "Data końcowa nie może być pusta") String endDate, @NotNull(message = "Kwota nie może być pusta") @Min(value = 1, message = "Kwota musi być dodatnia") Double amount) {
        this.username = username;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "NewMonthlyBudgetDto{" +
                "username='" + username + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", amount=" + amount +
                '}';
    }
}

