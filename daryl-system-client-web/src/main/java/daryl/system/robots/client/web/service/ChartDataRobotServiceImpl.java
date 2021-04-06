package daryl.system.robots.client.web.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.HistoricoOperaciones;
import daryl.system.robots.client.web.repository.IChartDataRobotRepository;

@Service
public class ChartDataRobotServiceImpl implements IChartDataRobotService {

	@Autowired
	IChartDataRobotRepository repository;

	@Transactional
	public List<HistoricoOperaciones> findListaParaChartByRobot(String robot) {
		return repository.findListaParaChartByRobot(robot);
	}

}
