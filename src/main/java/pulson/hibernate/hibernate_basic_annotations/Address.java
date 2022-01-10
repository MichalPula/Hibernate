package pulson.hibernate.hibernate_basic_annotations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Embeddable //Deklaracja, że klasa będzie zagnieżdżana w innych klasach
public class Address implements Serializable {

    private String country;

    private String city;

    private String street;

    private String building;

    public Address(String country, String city, String street, String building) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.building = building;
    }
}
