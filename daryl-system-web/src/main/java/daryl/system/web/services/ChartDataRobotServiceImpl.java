package daryl.system.web.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.HistoricoOperaciones;
import daryl.system.web.repository.IChartDataRobotRepository;

@Service
public class ChartDataRobotServiceImpl implements IChartDataRobotService {

	@Autowired
	IChartDataRobotRepository repository;

	@Transactional
	public List<HistoricoOperaciones> findListaParaChartByRobot(String robot) {
		
		final List<HistoricoOperaciones> hist = repository.findListaParaChartByRobot(robot);
		return hist;
	}

}
