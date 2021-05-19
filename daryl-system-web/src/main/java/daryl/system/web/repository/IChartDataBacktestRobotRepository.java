package daryl.system.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import daryl.system.model.backtest.HistoricoOperacionesBacktest;


@Repository
public interface IChartDataBacktestRobotRepository extends JpaRepository<HistoricoOperacionesBacktest, Long> {
	
	@Query("SELECT ho FROM HistoricoOperacionesBacktest ho WHERE ho.robot LIKE ?1% ORDER BY ho.id asc")
	List<HistoricoOperacionesBacktest> findListaParaChartByRobot(String robot);
	
}
