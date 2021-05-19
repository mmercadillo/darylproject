package daryl.system.web.services;

import daryl.system.model.ResumenRobot;
import daryl.system.model.backtest.ResumenRobotBacktest;


public interface IDetalleBacktestRobotService {
	ResumenRobotBacktest findResumenRobotBacktestByRobot(String robot);
}
