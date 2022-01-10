package pulson.hibernate.mapped_superclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employees", schema = "mapped_superclass")
public class Employee extends Person {

    private String companyName;

    public Employee(String personName, String companyName) {
        super(personName);
        this.companyName = companyName;
    }
}
