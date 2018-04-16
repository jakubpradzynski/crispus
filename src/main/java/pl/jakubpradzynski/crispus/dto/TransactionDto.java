package pl.jakubpradzynski.crispus.dto;

import pl.jakubpradzynski.crispus.domain.Transaction;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;

public class TransactionDto {

    @NotNull
    @Size(min = 1, max = 250, message = "Opis transakcji może mieć maksymalnie 250 znaków")
    private String description;

    @NotNull
    @Email
    private String username;

    @NotNull(message = "Nazwa konta nie może być pusta")
    @Size(min = 3, max = 30, message = "Nazwa konta musi mieć od 3 do 30 znaków")
    private String accountName;

    @NotNull
    private Double value;

    @NotNull
    private String date;

    private String placeDescription;

    private String transactionCategoryName;

    public static TransactionDto fromTransaction(Transaction transaction) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.description = transaction.getDescription();
        transactionDto.username = transaction.getUser().getEmail();
        transactionDto.accountName = transaction.getAccount().getName();
        transactionDto.value = transaction.getValue();
        transactionDto.date = format.format(transaction.getDate());
        transactionDto.placeDescription = transaction.getPlace() != null ? transaction.getPlace().getDescription() : null;
        transactionDto.transactionCategoryName = transaction.getTransactionCategory() != null ? transaction.getTransactionCategory().getName() : null;
        return transactionDto;
    }

    public TransactionDto() {
    }

    public TransactionDto(@NotNull @Email String username) {
        this.username = username;
    }

    public TransactionDto(@NotNull @Size(max = 250, message = "Opis transakcji może mieć maksymalnie 250 znaków") String description, @NotNull @Email String username, @NotNull(message = "Nazwa konta nie może być pusta") @Size(min = 3, max = 30, message = "Nazwa konta musi mieć od 3 do 30 znaków") String accountName, @NotNull Double value, @NotNull String date, String placeDescription, String transactionCategoryName) {
        this.description = description;
        this.username = username;
        this.accountName = accountName;
        this.value = value;
        this.date = date;
        this.placeDescription = placeDescription;
        this.transactionCategoryName = transactionCategoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public String getTransactionCategoryName() {
        return transactionCategoryName;
    }

    public void setTransactionCategoryName(String transactionCategoryName) {
        this.transactionCategoryName = transactionCategoryName;
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "description='" + description + '\'' +
                ", username='" + username + '\'' +
                ", accountName='" + accountName + '\'' +
                ", value=" + value +
                ", date='" + date + '\'' +
                ", placeDescription='" + placeDescription + '\'' +
                ", transactionCategoryName='" + transactionCategoryName + '\'' +
                '}';
    }
}
