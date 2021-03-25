package daryl.system.robots.totalpips.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.robots.totalpips.repo.ITotalPipsRobotsRepository;

@Service
public class ITotalPipsRobotsServiceImpl implements ITotalPipsRobotsService {

	@Autowired
	ITotalPipsRobotsRepository repository;
	
	@Override
	@Transactional
	public Long sumResumenRobots() {
		
		return repository.sumResumenRobots();
	}

	
	
}
