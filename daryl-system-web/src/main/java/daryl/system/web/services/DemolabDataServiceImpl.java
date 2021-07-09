package daryl.system.web.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.DemolabOps;
import daryl.system.web.repository.IDemolabDataRepository;

@Service
public class DemolabDataServiceImpl implements IDemolabDataService {

	@Autowired
	IDemolabDataRepository repository;

	@Transactional
	public List<DemolabOps> findListaParaChartDemolabByRobot(String robot) {
		final List<DemolabOps> test = repository.findListaParaChartDemolabByRobot(robot);
		return test;
	}

	@Transactional
	public List<DemolabOps> findAllByOrderByFcierre() {
		final List<DemolabOps> test = repository.findAllByOrderByFcierreAsc();
		return test;
	}
	
}
