package pulson.hibernate.inheritance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "juniors", schema = "inheritance")
@DiscriminatorValue("Junior Class")
@PrimaryKeyJoinColumn(name = "human_id")
//Używana przy strategii dziedziczenia JOINED. Definiuje kolumnę klucza głównego tabeli przechowującej obiekty podklasy, która jest używana jako klucz obcy w celu połączenia z tabelą przechowującą obiekty nadklasy
//W praktyce w tabeli juniors nazwa kolumny przechowującej klucz główny, zostanie zmieniona z 'id' na 'human_id'
public class Junior extends Human{

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "class_name")
    private String className;

    @NotNull
    @Column(name = "school_name")
    private String schoolName;

    public Junior(String firstName, String lastName, Date birthDate, String phoneNumber, String className, String schoolName) {
        super(firstName, lastName, birthDate);
        this.phoneNumber = phoneNumber;
        this.className = className;
        this.schoolName = schoolName;
    }
}
