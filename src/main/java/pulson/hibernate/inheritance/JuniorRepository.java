package pulson.hibernate.inheritance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuniorRepository extends JpaRepository<Junior, Long> {
}
