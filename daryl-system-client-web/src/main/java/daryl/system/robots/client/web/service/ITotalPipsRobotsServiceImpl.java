package daryl.system.robots.client.web.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.robots.client.web.repository.ITotalPipsRobotsRepository;

@Service
public class ITotalPipsRobotsServiceImpl implements ITotalPipsRobotsService {

	@Autowired
	ITotalPipsRobotsRepository repository;
	
	@Override
	@Transactional
	public Long sumResumenRobots() {
		
		return repository.sumResumenRobots();
	}

	
	@Transactional
	public Long totalPipsByRobot(String robot) {
		return repository.totalPipsByRobot(robot);
	}
	
}
