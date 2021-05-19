package daryl.system.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.ResumenRobot;
import daryl.system.model.backtest.ResumenRobotBacktest;

@Repository
public interface IDetalleBacktestRobotRepository extends JpaRepository<ResumenRobotBacktest, Long> {
	ResumenRobotBacktest findResumenRobotBacktestByRobot(String robot);
}
