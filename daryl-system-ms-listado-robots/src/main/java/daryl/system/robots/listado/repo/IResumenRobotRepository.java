package daryl.system.robots.listado.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.ResumenRobot;

@Repository
public interface IResumenRobotRepository extends JpaRepository<ResumenRobot, Long> {
	ResumenRobot findResumenRobotByRobotOrderByTotalDesc(String robot);
}
