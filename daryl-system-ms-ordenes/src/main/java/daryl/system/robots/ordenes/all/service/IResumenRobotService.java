package daryl.system.robots.ordenes.all.service;

import java.util.List;

import daryl.system.model.ResumenRobot;


public interface IResumenRobotService {
	List<ResumenRobot> findResumenRobotTopNumOrderByTotalDesc(Integer num);
}
