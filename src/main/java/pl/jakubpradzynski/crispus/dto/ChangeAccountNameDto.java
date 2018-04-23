package pl.jakubpradzynski.crispus.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChangeAccountNameDto {

    private String oldName;

    @NotNull(message = "Nazwa konta nie może być pusta")
    @Size(min = 3, max = 30, message = "Nazwa konta musi mieć od 3 do 30 znaków")
    private String newName;

    public ChangeAccountNameDto() {
    }

    public ChangeAccountNameDto(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    @Override
    public String toString() {
        return "ChangeAccountNameDto{" +
                "oldName='" + oldName + '\'' +
                ", newName='" + newName + '\'' +
                '}';
    }
}
