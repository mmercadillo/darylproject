package daryl.system.robots.robotsindarwin.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Darwin;
import daryl.system.model.RobotsDarwin;

@Repository
public interface IRobotInDarwinRepository extends JpaRepository<RobotsDarwin, Long> {
	List<RobotsDarwin> findRobotsDarwinsByRobotOrderByRobotDesc(String robot);
	List<RobotsDarwin> findRobotsDarwinsByDarwinOrderByRobotDesc(Darwin darwin);
}
