package daryl.system.backtest.robot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.backtest.OperacionBacktest;

@Repository
public interface IOperacionBacktestRepository extends JpaRepository<OperacionBacktest, Long> {
	
	Long deleteByRobot(String robot);
	
}
