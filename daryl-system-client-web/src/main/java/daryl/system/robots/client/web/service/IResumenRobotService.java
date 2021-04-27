package daryl.system.robots.client.web.service;

import java.util.List;

import daryl.system.model.ResumenRobot;


public interface IResumenRobotService {
	List<ResumenRobot> findResumenRobotTopNumOrderByTotalDesc(Integer num);
	ResumenRobot findResumenRobotByRobotOrderByTotalDesc(String robot);
	
	List<ResumenRobot> findResumenRobotsByRobotContainingIgnoreCaseOrderByEspmatDesc(String timeframe, Integer num);
	List<ResumenRobot> findResumenRobotsByRobotContainingIgnoreCaseOrderByTotalDesc(String timeframe, Integer num);
}
