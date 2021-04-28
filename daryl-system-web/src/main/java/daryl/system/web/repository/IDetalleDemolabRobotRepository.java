package daryl.system.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.ResumenRobotDemolab;

@Repository
public interface IDetalleDemolabRobotRepository extends JpaRepository<ResumenRobotDemolab, Long> {
	ResumenRobotDemolab findResumenRobotDemolabByRobot(String robot);
}
