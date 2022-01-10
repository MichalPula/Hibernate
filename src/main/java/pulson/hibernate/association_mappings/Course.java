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
@Table(name = "courses", schema = "association_mappings")
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    //Bidirectional ManyToOne
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    //Bidirectional ManyToMany
    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Student> students = new HashSet<>();


    public Course(String title, Teacher teacher) {
        this.title = title;
        this.teacher = teacher;
    }

    public Course(String title, Teacher teacher, Set<Student> students) {
        this.title = title;
        this.teacher = teacher;
        this.students = students;
    }
}

