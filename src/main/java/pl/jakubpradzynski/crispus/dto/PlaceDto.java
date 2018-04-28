package pl.jakubpradzynski.crispus.dto;

import pl.jakubpradzynski.crispus.domain.Place;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PlaceDto {

    @NotNull
    private Integer id;

    @NotNull(message = "Opis miejsca nie może być pusta")
    @Size(min = 3, max = 50, message = "Opis miejsca musi mieć od 3 do 50 znaków")
    private String description;

    public static PlaceDto fromPlace(Place place) {
        return new PlaceDto(place.getId(), place.getDescription());
    }

    public PlaceDto() {
    }

    public PlaceDto(@NotNull Integer id, @NotNull(message = "Opis miejsca nie może być pusta") @Size(min = 3, max = 50, message = "Opis miejsca musi mieć od 3 do 50 znaków") String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PlaceDto{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
