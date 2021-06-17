package daryl.system.robots.client.web.service;

import java.util.List;

import daryl.system.model.ResumenRobot;


public interface IResumenRobotService {
	List<ResumenRobot> findResumenRobotTopNumOrderByTotalDesc(Integer num);
	ResumenRobot findResumenRobotByRobotOrderByTotalDesc(String robot);
	
	List<ResumenRobot> findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByEspmatDesc(String timeframe, Long numOperaciones, Integer num);
	List<ResumenRobot> findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByTotalDesc(String timeframe, Long numOperaciones, Integer num);
}
