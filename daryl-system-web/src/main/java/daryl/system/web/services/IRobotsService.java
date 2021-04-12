package daryl.system.web.services;

import java.util.List;

import daryl.system.model.Robot;


public interface IRobotsService {
	List<Robot> findAll();
	public List<Robot> findAllByOrderByRobotAsc();
}
