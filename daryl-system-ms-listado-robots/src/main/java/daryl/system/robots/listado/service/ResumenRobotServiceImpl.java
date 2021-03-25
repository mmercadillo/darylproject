package daryl.system.robots.listado.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.ResumenRobot;
import daryl.system.robots.listado.repo.IResumenRobotRepository;

@Service
public class ResumenRobotServiceImpl implements IResumenRobotService {

	@Autowired
	IResumenRobotRepository repository;
	
	@Override
	@Transactional
	public ResumenRobot findResumenRobotByRobotOrderByTotalDesc(String robot) {
		
		return repository.findResumenRobotByRobotOrderByTotalDesc(robot);
	}

	
	
}
