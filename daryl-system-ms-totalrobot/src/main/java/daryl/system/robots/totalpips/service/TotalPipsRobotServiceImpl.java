package daryl.system.robots.totalpips.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.robots.totalpips.repo.ITotalPipsRobotRepository;

@Service
public class TotalPipsRobotServiceImpl implements ITotalPipsRobotService {

	@Autowired
	ITotalPipsRobotRepository repository;
	
	@Transactional
	public Long totalPipsByRobot(String robot) {
		return repository.totalPipsByRobot(robot);
	}

	
	
}
