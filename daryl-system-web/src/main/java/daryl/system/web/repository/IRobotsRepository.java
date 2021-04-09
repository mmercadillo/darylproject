package daryl.system.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.Robot;

@Repository
public interface IRobotsRepository extends JpaRepository<Robot, Long> {


}
