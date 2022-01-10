package pulson.hibernate.association_mappings.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pulson.hibernate.association_mappings.Teacher;
import pulson.hibernate.association_mappings.repositories.TeacherRepository;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }
}
