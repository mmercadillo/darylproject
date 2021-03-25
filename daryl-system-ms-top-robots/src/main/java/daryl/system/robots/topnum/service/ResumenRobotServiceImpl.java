package daryl.system.robots.topnum.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import daryl.system.model.ResumenRobot;
import daryl.system.robots.topnum.repo.IResumenRobotRepository;

@Service
public class ResumenRobotServiceImpl implements IResumenRobotService {

	@Autowired
	IResumenRobotRepository repository;
	
	@Override
	@Transactional
	public List<ResumenRobot> findResumenRobotTop5OrderByTotalDesc() {
		
		return repository.findResumenRobotsByOrderByTotalDesc(PageRequest.of(0,  5));
	}

	@Override
	@Transactional
	public List<ResumenRobot> findResumenRobotTopNumOrderByTotalDesc(Integer num) {
		
		return repository.findResumenRobotsByOrderByTotalDesc(PageRequest.of(0,  num));
	}	
	
}
