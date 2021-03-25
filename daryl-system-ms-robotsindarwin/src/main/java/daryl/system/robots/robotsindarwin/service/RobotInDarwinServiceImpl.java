package daryl.system.robots.robotsindarwin.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.comun.enums.Darwin;
import daryl.system.model.RobotsDarwin;
import daryl.system.robots.robotsindarwin.repo.IRobotInDarwinRepository;

@Service
public class RobotInDarwinServiceImpl implements IRobotInDarwinService {

	@Autowired
	IRobotInDarwinRepository repository;


	@Transactional
	public List<RobotsDarwin> findRobotsDarwinsByRobotOrderByRobotDesc(String robot) {
		return repository.findRobotsDarwinsByRobotOrderByRobotDesc(robot);
	}


	@Transactional
	public List<RobotsDarwin> findRobotsDarwinsByDarwinOrderByRobotDesc(Darwin darwin) {
		return repository.findRobotsDarwinsByDarwinOrderByRobotDesc(darwin);
	}

	
	
}
