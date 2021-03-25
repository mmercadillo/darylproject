package daryl.system.robots.listado.service;

import daryl.system.model.ResumenRobot;


public interface IResumenRobotService {
	ResumenRobot findResumenRobotByRobotOrderByTotalDesc(String robot);
}
