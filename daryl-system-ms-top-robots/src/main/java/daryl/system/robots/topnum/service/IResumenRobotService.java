package daryl.system.robots.topnum.service;

import java.util.List;

import daryl.system.model.ResumenRobot;


public interface IResumenRobotService {
	List<ResumenRobot> findResumenRobotTop5OrderByTotalDesc();
	List<ResumenRobot> findResumenRobotTopNumOrderByTotalDesc(Integer num);
}
