package daryl.system.web.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import daryl.system.model.ResumenRobot;


public interface IResumenRobotService {
	
	List<ResumenRobot> findResumenRobotTopNumOrderByEspmatDesc(Integer num);
	List<ResumenRobot> findResumenRobotTopNumOrderByTotalDesc(Integer num);
	ResumenRobot findResumenRobotByRobotOrderByTotalDesc(String robot);
	List<ResumenRobot> findResumenRobotsByRobotContainingIgnoreCaseOrderByEspmatDesc(String timeframe, Integer num);
	List<ResumenRobot> findResumenRobotsByRobotContainingIgnoreCaseOrderByTotalDesc(String timeframe, Integer num);
}
