package daryl.system.web.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import daryl.system.model.ResumenRobot;
import daryl.system.web.repository.IResumenRobotRepository;

@Service
public class ResumenRobotServiceImpl implements IResumenRobotService {

	@Autowired
	IResumenRobotRepository repository;

	@Transactional
	public List<ResumenRobot> findResumenRobotTopNumOrderByEspmatDesc(Integer num) {
		return repository.findResumenRobotsByOrderByEspmatDesc(PageRequest.of(0,  num));
	}
	
	@Transactional
	public List<ResumenRobot> findResumenRobotTopNumOrderByTotalDesc(Integer num) {
		return repository.findResumenRobotsByOrderByTotalDesc(PageRequest.of(0,  num));
	}

	@Transactional
	public ResumenRobot findResumenRobotByRobotOrderByTotalDesc(String robot) {
		return repository.findResumenRobotByRobotOrderByTotalDesc(robot);
	}
	
	@Transactional
	public ResumenRobot findResumenRobotByRobotOrderByEspmatDesc(String robot) {
		return repository.findResumenRobotByRobotOrderByEspmatDesc(robot);
	}

	@Transactional
	public List<ResumenRobot> findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByEspmatDesc(String timeframe, Long numOperaciones, Integer num){
		return repository.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByEspmatDesc(timeframe, numOperaciones, PageRequest.of(0,  num));
	}

	@Transactional
	public List<ResumenRobot> findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByTotalDesc(String timeframe, Long numOperaciones, Integer num){
		return repository.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByTotalDesc(timeframe, numOperaciones, PageRequest.of(0,  num));
	}
	
}
