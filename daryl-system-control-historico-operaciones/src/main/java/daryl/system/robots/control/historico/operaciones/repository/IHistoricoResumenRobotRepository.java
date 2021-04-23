package daryl.system.robots.control.historico.operaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.HistoricoResumenRobot;
import daryl.system.model.ResumenRobot;

@Repository
public interface IHistoricoResumenRobotRepository extends JpaRepository<HistoricoResumenRobot, Long> {

	HistoricoResumenRobot findHistoricoResumenRobotByIdResumen(Long idResumen);

}
