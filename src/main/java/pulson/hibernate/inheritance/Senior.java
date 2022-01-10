package pulson.hibernate.inheritance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "seniors", schema = "inheritance")
@DiscriminatorValue("Senior Class")
@PrimaryKeyJoinColumn(name = "human_id")
public class Senior extends Human {

    private String university;

    @Column(name = "years_as_a_senior")
    private Integer yearsAsASenior;

    public Senior(String firstName, String lastName, Date birthDate, String university, Integer yearsAsASenior) {
        super(firstName, lastName, birthDate);
        this.university = university;
        this.yearsAsASenior = yearsAsASenior;
    }
}
