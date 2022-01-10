package pulson.hibernate.association_mappings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "teachers", schema = "association_mappings")
public class Teacher implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

    //Bidirectional ManyToOne
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private Set<Course> courses = new HashSet<>();


    public Teacher(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Teacher(String firstName, String lastName, Set<Course> courses) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //Używane przy Bidirectional ManyToOne
    public void addCourse(Course course) {
        courses.add(course);
        course.setTeacher(this);
    }
    public void removeCourse(Course course) {
        courses.remove(course);
        course.setTeacher(null);
    }
}
