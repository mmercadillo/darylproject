package daryl.system.robots.detalle.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.ResumenRobot;

@Repository
public interface IDetalleRobotRepository extends JpaRepository<ResumenRobot, Long> {
	ResumenRobot findResumenRobotByRobot(String robot);
}
