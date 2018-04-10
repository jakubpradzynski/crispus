package pl.jakubpradzynski.crispus.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity(name = "TRANSACTION_CATEGORY")
@Table(name = "TRANSACTION_CATEGORY")
public class TransactionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "ID")
    private Integer id;

    @NotNull(message = "Nazwa kategorii transakcji nie może być pusta")
    @Size(min = 3, max = 50, message = "Nazwa kategorii transakcji musi mieć od 3 do 50 znaków")
    @Column(name = "NAME")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_FK")
    private Set<User> users;

    public TransactionCategory() {
    }

    public TransactionCategory(@NotNull(message = "Nazwa kategorii transakcji nie może być pusta") @Size(min = 3, max = 50, message = "Nazwa kategorii transakcji musi mieć od 3 do 50 znaków") String name, Set<User> users) {
        this.name = name;
        this.users = users;
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "TransactionCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users +
                '}';
    }
}
