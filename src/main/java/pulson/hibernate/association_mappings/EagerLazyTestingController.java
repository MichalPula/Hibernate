package pulson.hibernate.association_mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pulson.hibernate.association_mappings.services.CourseMaterialService;
import pulson.hibernate.association_mappings.services.CourseService;
import pulson.hibernate.association_mappings.services.TeacherService;

import java.util.List;

@RestController
@RequestMapping("/eagerlazy")
public class EagerLazyTestingController {

    private final CourseService courseService;
    private final TeacherService teacherService;
    private final CourseMaterialService courseMaterialService;

    @Autowired
    public EagerLazyTestingController(CourseService courseService, TeacherService teacherService, CourseMaterialService courseMaterialService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.courseMaterialService = courseMaterialService;
    }

    @GetMapping(value = "/teachers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok().body(teacherService.getAll());
    }

    @GetMapping(value = "/courses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok().body(courseService.getAll());
    }

    @GetMapping(value = "/materials", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseMaterial>> getAllMaterials() {
        return ResponseEntity.ok().body(courseMaterialService.getAll());
    }
}
