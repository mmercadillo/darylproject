package daryl.system.robots.control.historico.operaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.HistoricoResumenRobot;

@Repository
public interface IHistoricoResumenRobotRepository extends JpaRepository<HistoricoResumenRobot, Long> {


}
