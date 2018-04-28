package pl.jakubpradzynski.crispus.dto;

import javax.validation.constraints.NotNull;

public class RemoveAccountDto {

    private String username;

    @NotNull
    private String name;

    public RemoveAccountDto() {
    }

    public RemoveAccountDto(@NotNull String username, @NotNull String name) {
        this.username = username;
        this.name = name;
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

    @Override
    public String toString() {
        return "RemoveAccountDto{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
