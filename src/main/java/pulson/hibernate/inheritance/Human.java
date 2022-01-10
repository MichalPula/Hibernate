package pulson.hibernate.inheritance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "humans", schema = "inheritance")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//InheritanceType.SINGLE_TABLE - defaultowa strategia. Wszystkie pola nadklasy(Human) i podklas(Junior, Senior) będą znajdowały się w jednej tabeli 'humans'. W miejsce pól, których dana klasa nie posiada, wstawiany jest NULL. Przez brak joinów performance SELECT jest na wysokim poziomie
//InheritanceType.TABLE_PER_CLASS - dla nadklasy i wszystkich podklas zostaną stworzone osobne tabele przechowujące zarówno wspólne, jak i specyficzne pola. Przy podklasach Junior i Senior i dodaniu po 3 rekordów - Students będą mieli id 1,2,3 a Seniors 4,5,6. Przy tej strategii nie można używać key generation IDENTITY.
//InheritanceType.JOINED - wszystkie dziedziczone pola nadklasy zostaną zapisane w tabeli humans, a pola specyficzne dla podklas - w odpowiadających im tabelach. W tabeli humans id będą miały wartości od 1 do 6, a w tabelach podklas w kolumnie human_id - 1,2,3 i 4,5,6.
//Wadą tej strategii jest to, że przy wyciąganiu z bazy danych między tabelami używany jest join. Oznacza to, że SELECT nadklasy wyciągnie pola wszystkich swoich podklas. Im wyżej w hierarchii, tym więcej tabel joinów.

@DiscriminatorColumn(name = "human_type", discriminatorType = DiscriminatorType.STRING)
//Przy typie dziedziczenia SINGLE_TABLE w tabeli humans utworzy się kolumna "dtype". Pozwala ona rozróżnić konkretny typ podklasy będący rekordem. Gdyby po klasie Human dziedziczyło więcej klas, każdy rekord(instancja) zostałby opisany odpowiednią wartością w kolumnie dtype (np. Junior, Senior, Mid itp)
//Dzięki tej adnotacji możemy zmienić nazwę z dtype na human_type. Z kolei adnotacja @DiscriminatorValue("Junior/Senior Class") zamieszczona w podklasach ustala nazwy, które zostaną wprowadzone do kolumny 'human_type', czyli jak ma nazywać się nasz Junior i inne obiekty podklas (defaultowo jest to po prostu nazwa klasy).
//@DiscriminatorColumn można używać też z InheritanceType.JOINED - wtedy nasze 'human_type' zostanie dodane do tabeli nadklasy, czyli humans (defaultowo kolumna ta nie występuje)
public class Human {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, updatable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    public Human(String firstName, String lastName, Date birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }
}
