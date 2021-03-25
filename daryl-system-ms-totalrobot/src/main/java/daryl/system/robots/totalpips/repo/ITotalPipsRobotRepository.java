package daryl.system.robots.totalpips.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import daryl.system.model.HistoricoOperaciones;

@Repository
public interface ITotalPipsRobotRepository extends JpaRepository<HistoricoOperaciones, Long> {
	@Query("SELECT sum(ho.profit) FROM HistoricoOperaciones ho WHERE ho.comentario LIKE ?1%")
	Long totalPipsByRobot(String robot);
}
