package daryl.system.web.services;

import java.util.List;

import daryl.system.model.ResumenRobot;


public interface IResumenRobotService {
	
	List<ResumenRobot> findResumenRobotTopNumOrderByEspmatDesc(Integer num);
	List<ResumenRobot> findResumenRobotTopNumOrderByTotalDesc(Integer num);
	ResumenRobot findResumenRobotByRobotOrderByTotalDesc(String robot);
}
