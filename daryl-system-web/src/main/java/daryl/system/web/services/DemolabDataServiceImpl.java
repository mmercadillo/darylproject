package daryl.system.web.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.DemolabOps;
import daryl.system.model.HistoricoOperaciones;
import daryl.system.web.repository.IChartDataRobotRepository;
import daryl.system.web.repository.IDemolabDataRepository;

@Service
public class DemolabDataServiceImpl implements IDemolabDataService {

	@Autowired
	IDemolabDataRepository repository;

	@Transactional
	public List<DemolabOps> findListaParaChartDemolabByRobot(String robot) {
		return repository.findListaParaChartDemolabByRobot(robot);
	}

}
