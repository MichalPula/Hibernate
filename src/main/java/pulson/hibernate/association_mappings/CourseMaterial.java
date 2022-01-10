package pulson.hibernate.association_mappings;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "course_materials", schema = "association_mappings")
public class CourseMaterial implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String url;

    //Unidirectional OneToOne
    @OneToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    public CourseMaterial(String url) {
        this.url = url;
    }

    public CourseMaterial(String url, Course course) {
        this.url = url;
        this.course = course;
    }
}
