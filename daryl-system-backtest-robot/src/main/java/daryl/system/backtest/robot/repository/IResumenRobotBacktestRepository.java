package daryl.system.backtest.robot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.ResumenRobot;
import daryl.system.model.backtest.ResumenRobotBacktest;

@Repository
public interface IResumenRobotBacktestRepository extends JpaRepository<ResumenRobotBacktest, Long> {

	ResumenRobotBacktest findResumenRobotByRobot(String robot);

}
