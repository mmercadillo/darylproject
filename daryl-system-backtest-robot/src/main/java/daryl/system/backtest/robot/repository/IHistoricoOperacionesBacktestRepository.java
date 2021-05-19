package daryl.system.backtest.robot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import daryl.system.model.backtest.HistoricoOperacionesBacktest;

@Repository
public interface IHistoricoOperacionesBacktestRepository extends JpaRepository<HistoricoOperacionesBacktest, Long> {
	
	Long deleteByRobot(String robot);
	
	@Query("SELECT ho FROM HistoricoOperacionesBacktest ho WHERE ho.robot LIKE ?1% and ho.id > ?2 ORDER BY ho.id asc")
	List<HistoricoOperacionesBacktest> findListaByRobot(String robot, Long id);
	
}
