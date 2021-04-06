package daryl.system.robots.client.web.service;

import java.util.List;

import daryl.system.model.Orden;


public interface IOrdenesService {
	List<Orden> findAllByOrderByRobotAsc();
	List<Orden> findAllByRobots(List<String> robots);
}
