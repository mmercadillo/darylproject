package daryl.system.robots.client.web.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import daryl.system.model.ResumenRobot;
import daryl.system.robots.client.web.repository.IResumenRobotRepository;

@Service
public class ResumenRobotServiceImpl implements IResumenRobotService {

	@Autowired
	IResumenRobotRepository repository;
	
	@Transactional
	public List<ResumenRobot> findResumenRobotTopNumOrderByTotalDesc(Integer num) {
		
		return repository.findResumenRobotsByOrderByTotalDesc(PageRequest.of(0,  num));
	}

	
	@Override
	@Transactional
	public ResumenRobot findResumenRobotByRobotOrderByTotalDesc(String robot) {
		
		return repository.findResumenRobotByRobotOrderByTotalDesc(robot);
	}
	
	@Transactional
	public List<ResumenRobot> findResumenRobotsByRobotContainingIgnoreCaseOrderByEspmatDesc(String timeframe, Integer num){
		return repository.findResumenRobotsByRobotContainingIgnoreCaseOrderByEspmatDesc(timeframe, PageRequest.of(0,  num));
	}

	@Transactional
	public List<ResumenRobot> findResumenRobotsByRobotContainingIgnoreCaseOrderByTotalDesc(String timeframe, Integer num){
		return repository.findResumenRobotsByRobotContainingIgnoreCaseOrderByTotalDesc(timeframe, PageRequest.of(0,  num));
	}
}
