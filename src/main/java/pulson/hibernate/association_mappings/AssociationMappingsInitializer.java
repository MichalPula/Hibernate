package pulson.hibernate.association_mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pulson.hibernate.association_mappings.repositories.CourseMaterialRepository;
import pulson.hibernate.association_mappings.repositories.CourseRepository;
import pulson.hibernate.association_mappings.repositories.StudentRepository;
import pulson.hibernate.association_mappings.repositories.TeacherRepository;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class AssociationMappingsInitializer {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseMaterialRepository courseMaterialRepository;

    @Autowired
    public AssociationMappingsInitializer(TeacherRepository teacherRepository, StudentRepository studentRepository,
                                          CourseMaterialRepository courseMaterialRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.courseMaterialRepository = courseMaterialRepository;

        //initialize();
    }

    @Transactional
    void initialize() {
        Teacher teacher = new Teacher("Scooby", "Doo", new HashSet<>());

        Course course1 = new Course("Java", teacher, new HashSet<>());
        Course course2 = new Course("SQL", teacher, new HashSet<>());
        Course course3 = new Course("HTML", teacher, new HashSet<>());
        teacher.addCourse(course1);
        teacher.addCourse(course2);
        teacher.addCourse(course3);
        teacherRepository.save(teacher);

        Student student1 = new Student("student1", "student1");
        student1.addCourse(course1);
        student1.addCourse(course2);
        student1.addCourse(course3);
        Student student2 = new Student("student2", "student2");
        student2.addCourse(course3);
        studentRepository.saveAll(Arrays.asList(student1, student2));

        CourseMaterial courseMaterial1 = new CourseMaterial("javaurl", course1);
        CourseMaterial courseMaterial2 = new CourseMaterial("sqlurl", course2);
        CourseMaterial courseMaterial3 = new CourseMaterial("htmlurl", course3);
        courseMaterialRepository.saveAll(Arrays.asList(courseMaterial1, courseMaterial2, courseMaterial3));
    }
}
