package daryl.system.web.services;

import daryl.system.model.ResumenRobotDemolab;


public interface IDetalleDemolabRobotService {
	ResumenRobotDemolab findResumenRobotDemolabByRobot(String robot);
}
