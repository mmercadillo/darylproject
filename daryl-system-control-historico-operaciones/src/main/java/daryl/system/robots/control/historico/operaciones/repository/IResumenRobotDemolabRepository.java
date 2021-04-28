package daryl.system.robots.control.historico.operaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.ResumenRobot;
import daryl.system.model.ResumenRobotDemolab;

@Repository
public interface IResumenRobotDemolabRepository extends JpaRepository<ResumenRobotDemolab, Long> {

	ResumenRobotDemolab findResumenRobotDemolabByRobot(String robot);

}
