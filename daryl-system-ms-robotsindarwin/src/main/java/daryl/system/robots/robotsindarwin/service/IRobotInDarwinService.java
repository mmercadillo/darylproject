package daryl.system.robots.robotsindarwin.service;

import java.util.List;

import daryl.system.comun.enums.Darwin;
import daryl.system.model.RobotsDarwin;


public interface IRobotInDarwinService {
	List<RobotsDarwin> findRobotsDarwinsByRobotOrderByRobotDesc(String robot);
	List<RobotsDarwin> findRobotsDarwinsByDarwinOrderByRobotDesc(Darwin darwin);
}
