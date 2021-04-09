package daryl.system.web.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.web.repository.ITotalPipsRobotsRepository;

@Service
public class TotalPipsRobotsServiceImpl implements ITotalPipsRobotsService {

	@Autowired
	ITotalPipsRobotsRepository repository;
	
	@Transactional
	public Long sumResumenRobots() {
		
		return repository.sumResumenRobots();
	}

	@Transactional
	public Long totalPipsByRobot(String robot) {
		return repository.totalPipsByRobot(robot);
	}
	
}
