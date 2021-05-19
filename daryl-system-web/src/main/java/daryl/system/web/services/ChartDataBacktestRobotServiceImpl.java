package daryl.system.web.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.HistoricoOperaciones;
import daryl.system.model.backtest.HistoricoOperacionesBacktest;
import daryl.system.web.repository.IChartDataBacktestRobotRepository;
import daryl.system.web.repository.IChartDataRobotRepository;

@Service
public class ChartDataBacktestRobotServiceImpl implements IChartDataBacktestRobotService{

	@Autowired
	IChartDataBacktestRobotRepository repository;

	@Transactional
	public List<HistoricoOperacionesBacktest> findListaParaChartByRobot(String robot) {
		return repository.findListaParaChartByRobot(robot);
	}

}
