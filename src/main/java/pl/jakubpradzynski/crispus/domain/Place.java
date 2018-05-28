package pl.jakubpradzynski.crispus.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity(name = "PLACE")
@Table(name = "PLACE")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_id_generator")
    @SequenceGenerator(name="place_id_generator", sequenceName="place_seq_for_id", initialValue = 1, allocationSize = 1)
    @NotNull
    @Column(name = "ID")
    private Integer id;

    @NotNull(message = "Opis miejsca nie może być pusta")
    @Size(min = 3, max = 50, message = "Opis miejsca musi mieć od 3 do 50 znaków")
    @Column(name = "NAME", unique=true)
    private String name; // TODO change to name

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "USER_FK")
    @JoinTable(name = "user_place",
            joinColumns = { @JoinColumn(name = "place_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") })
    private Set<User> users;

    @NotNull
    @Column(name = "IS_PREDEFINED")
    private Character isPredefined;

    public Place() {
    }

    public Place(@NotNull(message = "Opis miejsca nie może być pusta") @Size(min = 3, max = 50, message = "Opis miejsca musi mieć od 3 do 50 znaków") String name, Set<User> users, Character isPredefined) {
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
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isPredefined=" + isPredefined +
                '}';
    }
}
