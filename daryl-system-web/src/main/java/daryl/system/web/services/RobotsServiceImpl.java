package daryl.system.web.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.Robot;
import daryl.system.web.repository.IRobotsRepository;

@Service
public class RobotsServiceImpl implements IRobotsService {

	@Autowired
	IRobotsRepository repository;

	@Transactional
	public List<Robot> findAll() {

		return repository.findAll();
	}

	public List<Robot> findAllByOrderByRobotAsc() {
		return repository.findAllByOrderByRobotAsc();
	}

	@Override
	public Robot findRobotByRobot(String robot) {
		return repository.findRobotByRobot(robot);
	}
	

	
	
}
