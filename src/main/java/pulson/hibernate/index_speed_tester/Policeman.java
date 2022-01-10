package pulson.hibernate.index_speed_tester;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "policemen", schema = "index_speed_tester", indexes = {@Index(name = "first_name_index", columnList = "first_name")})
public class Policeman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, updatable = false)
    private Long id;

    @Column(name = "first_name")//Wymagane, aby stworzyć index
    private String firstName;

    private String lastName;

    private String uniqueIdentifier;

    public Policeman(String firstName, String lastName, String uniqueIdentifier) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.uniqueIdentifier = uniqueIdentifier;
    }
}
