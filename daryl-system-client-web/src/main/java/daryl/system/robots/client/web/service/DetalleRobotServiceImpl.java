package daryl.system.robots.client.web.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.ResumenRobot;
import daryl.system.robots.client.web.repository.IDetalleRobotRepository;

@Service
public class DetalleRobotServiceImpl implements IDetalleRobotService {

	@Autowired
	IDetalleRobotRepository repository;

	@Transactional
	public ResumenRobot findResumenRobotByRobot(String robot) {
		return repository.findResumenRobotByRobot(robot);
	}



	
	
}
