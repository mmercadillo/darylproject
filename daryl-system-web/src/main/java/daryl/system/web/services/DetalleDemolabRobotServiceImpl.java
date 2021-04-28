package daryl.system.web.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.ResumenRobotDemolab;
import daryl.system.web.repository.IDetalleDemolabRobotRepository;

@Service
public class DetalleDemolabRobotServiceImpl implements IDetalleDemolabRobotService {

	@Autowired
	IDetalleDemolabRobotRepository repository;

	@Transactional
	public ResumenRobotDemolab findResumenRobotDemolabByRobot(String robot) {
		return repository.findResumenRobotDemolabByRobot(robot);
	}



	
	
}
