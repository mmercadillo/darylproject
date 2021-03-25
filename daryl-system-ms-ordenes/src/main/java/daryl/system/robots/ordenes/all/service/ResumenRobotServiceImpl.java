package daryl.system.robots.ordenes.all.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import daryl.system.model.ResumenRobot;
import daryl.system.robots.ordenes.all.repo.IResumenRobotRepository;

@Service
public class ResumenRobotServiceImpl implements IResumenRobotService {

	@Autowired
	IResumenRobotRepository repository;
	
	@Transactional
	public List<ResumenRobot> findResumenRobotTopNumOrderByTotalDesc(Integer num) {
		
		return repository.findResumenRobotsByOrderByTotalDesc(PageRequest.of(0,  num));
	}

	
	
}
