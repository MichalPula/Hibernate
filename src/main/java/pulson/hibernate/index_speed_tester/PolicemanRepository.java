package pulson.hibernate.index_speed_tester;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicemanRepository extends JpaRepository<Policeman, Long> {
}
