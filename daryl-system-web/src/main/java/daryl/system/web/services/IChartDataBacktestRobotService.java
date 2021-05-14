package daryl.system.web.services;

import java.util.List;

import daryl.system.model.backtest.HistoricoOperacionesBacktest;


public interface IChartDataBacktestRobotService {
	List<HistoricoOperacionesBacktest> findListaParaChartByRobot(String robot);
	
}
