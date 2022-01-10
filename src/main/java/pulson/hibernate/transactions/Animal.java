package pulson.hibernate.transactions;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name = "animals")
@Table(name = "animals", schema = "spring_transactions")
@NamedQueries({
        @NamedQuery(name = "animals.getAnimalsByNameLength",
                query = "select a from animals a where length(a.name) = :length")
})//entityManager.createNamedQuery("animals.getAnimalsByNameLength").setParameter("length", 5).getResultList()
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false)
    private Long id;

    private String name;

    public Animal(String name) {
        this.name = name;
    }
}
