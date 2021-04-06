package daryl.system.robots.client.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import daryl.system.model.HistoricoOperaciones;


@Repository
public interface IChartDataRobotRepository extends JpaRepository<HistoricoOperaciones, Long> {
	
	@Query("SELECT ho FROM HistoricoOperaciones ho WHERE ho.comentario LIKE ?1% ORDER BY ho.id asc")
	List<HistoricoOperaciones> findListaParaChartByRobot(String robot);
}
