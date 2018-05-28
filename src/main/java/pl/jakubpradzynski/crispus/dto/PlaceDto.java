package pl.jakubpradzynski.crispus.dto;

import pl.jakubpradzynski.crispus.domain.Place;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PlaceDto implements Comparable<PlaceDto> {

    private Integer id;

    @NotNull(message = "Opis miejsca nie może być pusta")
    @Size(min = 3, max = 50, message = "Opis miejsca musi mieć od 3 do 50 znaków")
    private String name;

    private Character isPreDefined;

    public static PlaceDto fromPlace(Place place) {
        return new PlaceDto(place.getId(), place.getName(), place.getPredefined());
    }

    public PlaceDto() {
    }

    public PlaceDto(Integer id, @NotNull(message = "Opis miejsca nie może być pusta") @Size(min = 3, max = 50, message = "Opis miejsca musi mieć od 3 do 50 znaków") String name) {
        this.id = id;
        this.name = name;
    }

    public PlaceDto(Integer id, @NotNull(message = "Opis miejsca nie może być pusta") @Size(min = 3, max = 50, message = "Opis miejsca musi mieć od 3 do 50 znaków") String name, Character isPreDefined) {
        this.id = id;
        this.name = name;
        this.isPreDefined = isPreDefined;
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
        return "PlaceDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isPreDefined=" + isPreDefined +
                '}';
    }

    @Override
    public int compareTo(PlaceDto o) {
        return this.name.compareToIgnoreCase(o.name);
    }
}
