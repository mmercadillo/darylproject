package daryl.system.web.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.backtest.ResumenRobotBacktest;
import daryl.system.web.repository.IDetalleBacktestRobotRepository;

@Service
public class DetalleBacktestRobotServiceImpl implements IDetalleBacktestRobotService {

	@Autowired
	IDetalleBacktestRobotRepository repository;

	@Transactional
	public ResumenRobotBacktest findResumenRobotBacktestByRobot(String robot) {
		return repository.findResumenRobotBacktestByRobot(robot);
	}



	
	
}
