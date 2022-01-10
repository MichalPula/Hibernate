package pulson.hibernate.association_mappings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pulson.hibernate.association_mappings.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
