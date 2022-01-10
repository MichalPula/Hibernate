package pulson.hibernate.mapped_superclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
//Dziedziczenie pozostaje na poziomie klasy i obiektu. Nie jest widoczne w bazie danych i nie ma na nią wpływu.
//Nadklasa person nie jest zapisywana do bazy danych. Jej pola zostaną tylko dołączone do tabel przechowujących rekordy podklas.
//W bazie pojawi się tabela employees z kolumnami person_id, person_name i company_name.
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    private String personName;

    public Person(String personName) {
        this.personName = personName;
    }
}
