package pl.jakubpradzynski.crispus.dto;

import pl.jakubpradzynski.crispus.domain.MonthlyBudget;

import java.util.Date;

public class MonthlyBudgetInfoDto {

    private Integer id;

    private String username;

    private Date startDate;

    private Date endDate;

    private Double amount;

    private Double usedAmount;

    private Double difference;

    public static MonthlyBudgetInfoDto fromMonthlyBudget(MonthlyBudget monthlyBudget) {
        return new MonthlyBudgetInfoDto(
                monthlyBudget.getId(),
                monthlyBudget.getUser().getEmail(),
                monthlyBudget.getStartDate(),
                monthlyBudget.getEndDate(),
                monthlyBudget.getAmount()
        );
    }

    public MonthlyBudgetInfoDto() {
    }

    public MonthlyBudgetInfoDto(Integer id, String username, Date startDate, Date endDate, Double amount) {
        this.id = id;
        this.username = username;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
    }

    public MonthlyBudgetInfoDto(Integer id, String username, Date startDate, Date endDate, Double amount, Double usedAmount, Double difference) {
        this.id = id;
        this.username = username;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.usedAmount = usedAmount;
        this.difference = difference;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(Double usedAmount) {
        this.usedAmount = usedAmount;
    }

    public Double getDifference() {
        return difference;
    }

    public void setDifference(Double difference) {
        this.difference = difference;
    }

    @Override
    public String toString() {
        return "MonthlyBudgetInfoDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", amount=" + amount +
                ", usedAmount=" + usedAmount +
                ", difference=" + difference +
                '}';
    }
}
