package pl.jakubpradzynski.crispus.dto;

import pl.jakubpradzynski.crispus.domain.TransactionCategory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TransactionCategoryDto implements Comparable<TransactionCategoryDto> {

    private Integer id;

    @NotNull(message = "Nazwa kategorii transakcji nie może być pusta")
    @Size(min = 3, max = 50, message = "Nazwa kategorii transakcji musi mieć od 3 do 50 znaków")
    private String name;

    private boolean isPreDefined;

    public static TransactionCategoryDto fromTransactionCategory(TransactionCategory transactionCategory) {
        return new TransactionCategoryDto(transactionCategory.getId(), transactionCategory.getName(), transactionCategory.getUsers().isEmpty());
    }

    public TransactionCategoryDto() {
    }

    public TransactionCategoryDto(Integer id, @NotNull(message = "Nazwa kategorii transakcji nie może być pusta") @Size(min = 3, max = 50, message = "Nazwa kategorii transakcji musi mieć od 3 do 50 znaków") String name, boolean isPreDefined) {
        this.id = id;
        this.name = name;
        this.isPreDefined = isPreDefined;
    }

    public TransactionCategoryDto(Integer id, @NotNull(message = "Nazwa kategorii transakcji nie może być pusta") @Size(min = 3, max = 50, message = "Nazwa kategorii transakcji musi mieć od 3 do 50 znaków") String name) {
        this.id = id;
        this.name = name;
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

    public boolean isPreDefined() {
        return isPreDefined;
    }

    public void setPreDefined(boolean preDefined) {
        isPreDefined = preDefined;
    }

    @Override
    public String toString() {
        return "TransactionCategoryDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isPreDefined=" + isPreDefined +
                '}';
    }

    @Override
    public int compareTo(TransactionCategoryDto o) {
        return this.name.compareToIgnoreCase(o.name);
    }
}
