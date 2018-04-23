package pl.jakubpradzynski.crispus.dto;

public class AccountValuesDto {

    private Integer id;
    private String accountName;
    private String username;
    private Double expensesAmount;
    private Double revenuesAmount;
    private Double moneyAmount;

    public AccountValuesDto() {
    }

    public AccountValuesDto(Integer id, String accountName, String username, Double expensesAmount, Double revenuesAmount, Double moneyAmount) {
        this.id = id;
        this.accountName = accountName;
        this.username = username;
        this.expensesAmount = expensesAmount;
        this.revenuesAmount = revenuesAmount;
        this.moneyAmount = moneyAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getExpensesAmount() {
        return expensesAmount;
    }

    public void setExpensesAmount(Double expensesAmount) {
        this.expensesAmount = expensesAmount;
    }

    public Double getRevenuesAmount() {
        return revenuesAmount;
    }

    public void setRevenuesAmount(Double revenuesAmount) {
        this.revenuesAmount = revenuesAmount;
    }

    public Double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "AccountValuesDto{" +
                "id=" + id +
                ", accountName='" + accountName + '\'' +
                ", username='" + username + '\'' +
                ", expensesAmount=" + expensesAmount +
                ", revenuesAmount=" + revenuesAmount +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}
