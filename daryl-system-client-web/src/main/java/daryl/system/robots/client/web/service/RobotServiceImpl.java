package daryl.system.robots.client.web.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.Robot;
import daryl.system.robots.client.web.repository.IRobotsRepository;

@Service
public class RobotServiceImpl implements IRobotService {

	@Autowired
	IRobotsRepository repository;
	
	@Override
	@Transactional
	public List<Robot> findAll() {
		
		return repository.findAll();
	}

	
	
}
