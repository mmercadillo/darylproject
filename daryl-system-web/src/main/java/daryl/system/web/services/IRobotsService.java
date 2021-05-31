package daryl.system.web.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import daryl.system.model.ResumenRobot;
import daryl.system.model.Robot;


public interface IRobotsService {
	
	List<Robot> findAll();
	List<Robot> findAllByOrderByRobotAsc();
	Robot findRobotByRobot(String robot);
	
	List<Robot> findRobotByRobotContainingIgnoreCaseOrderByRobotDesc(String robot);
}
