package daryl.system.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.Robot;

@Repository
public interface IRobotsRepository extends JpaRepository<Robot, Long> {

	List<Robot> findAllByOrderByRobotAsc();
	Robot findRobotByRobot(String robot);
	List<Robot> findRobotByRobotContainingIgnoreCaseOrderByRobotDesc(String robot);
}
