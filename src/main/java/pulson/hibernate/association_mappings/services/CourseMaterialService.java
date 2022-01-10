package pulson.hibernate.association_mappings.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pulson.hibernate.association_mappings.CourseMaterial;
import pulson.hibernate.association_mappings.repositories.CourseMaterialRepository;

import java.util.List;

@Service
public class CourseMaterialService {

    private final CourseMaterialRepository courseMaterialRepository;

    @Autowired
    public CourseMaterialService(CourseMaterialRepository courseMaterialRepository) {
        this.courseMaterialRepository = courseMaterialRepository;
    }

    public List<CourseMaterial> getAll() {
        return courseMaterialRepository.findAll();
    }
}
