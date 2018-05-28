package pl.jakubpradzynski.crispus.dto;

import pl.jakubpradzynski.crispus.domain.Category;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategoryDto implements Comparable<CategoryDto> {

    private Integer id;

    @NotNull(message = "Nazwa kategorii transakcji nie może być pusta")
    @Size(min = 3, max = 50, message = "Nazwa kategorii transakcji musi mieć od 3 do 50 znaków")
    private String name;

    private Character isPreDefined;

    public static CategoryDto fromTransactionCategory(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getPredefined());
    }

    public CategoryDto() {
    }

    public CategoryDto(Integer id, @NotNull(message = "Nazwa kategorii transakcji nie może być pusta") @Size(min = 3, max = 50, message = "Nazwa kategorii transakcji musi mieć od 3 do 50 znaków") String name, Character isPreDefined) {
        this.id = id;
        this.name = name;
        this.isPreDefined = isPreDefined;
    }

    public CategoryDto(Integer id, @NotNull(message = "Nazwa kategorii transakcji nie może być pusta") @Size(min = 3, max = 50, message = "Nazwa kategorii transakcji musi mieć od 3 do 50 znaków") String name) {
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

    public Character isPreDefined() {
        return isPreDefined;
    }

    public void setPreDefined(Character preDefined) {
        isPreDefined = preDefined;
    }

    @Override
    public String toString() {
        return "CategoryDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isPreDefined=" + isPreDefined +
                '}';
    }

    @Override
    public int compareTo(CategoryDto o) {
        return this.name.compareToIgnoreCase(o.name);
    }
}
