package daryl.system.robot.full.node.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.AnnConfig;


@Repository
public interface IAnnConfigRepository extends JpaRepository<AnnConfig, Long> {

	AnnConfig findAnnConfigByRobot(String robot);

}
