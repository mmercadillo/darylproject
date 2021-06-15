package daryl.system.robot.full.node.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.RnaConfig;


@Repository
public interface IRnaConfigRepository extends JpaRepository<RnaConfig, Long> {

	RnaConfig findRnaConfigByRobot(String robot);

}
