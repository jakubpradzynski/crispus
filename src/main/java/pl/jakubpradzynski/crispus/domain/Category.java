package pl.jakubpradzynski.crispus.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity(name = "CATEGORY")
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_generator")
    @SequenceGenerator(name="category_id_generator", sequenceName="category_seq_for_id", initialValue = 1, allocationSize = 1)
    @NotNull
    @Column(name = "ID")
    private Integer id;

    @NotNull(message = "Nazwa kategorii transakcji nie może być pusta")
    @Size(min = 3, max = 50, message = "Nazwa kategorii transakcji musi mieć od 3 do 50 znaków")
    @Column(name = "NAME", unique=true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_category",
            joinColumns = { @JoinColumn(name = "category_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") })
    private Set<User> users;

    @NotNull
    @Column(name = "IS_PREDEFINED")
    private Character isPredefined;

    public Category() {
    }

    public Category(@NotNull(message = "Nazwa kategorii transakcji nie może być pusta") @Size(min = 3, max = 50, message = "Nazwa kategorii transakcji musi mieć od 3 do 50 znaków") String name, Set<User> users, Character isPredefined) {
        this.name = name;
        this.users = users;
        this.isPredefined = isPredefined;
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

    public Character getPredefined() {
        return isPredefined;
    }

    public void setPredefined(Character predefined) {
        isPredefined = predefined;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isPredefined=" + isPredefined +
                '}';
    }
}
