package daryl.system.robots.ordenes.all.service;

import java.util.List;

import daryl.system.model.ResumenRobot;
import daryl.system.model.RobotsCuenta;


public interface IRobotsCuentaService {
	List<RobotsCuenta> findRobotsCuentaByCuenta(String cuenta);
}
