package daryl.system.web.services;

import java.util.List;

import daryl.system.model.ResumenRobot;
import daryl.system.model.RobotsCuenta;


public interface IRobotsCuentaService {
	List<RobotsCuenta> findRobotsCuentaByCuenta(String cuenta);
	void saveAll(List<RobotsCuenta> robotCuenta);
	void deleteAll(List<RobotsCuenta> robotCuenta);
}
