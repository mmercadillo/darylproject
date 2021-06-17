package daryl.system.web.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import daryl.system.model.ResumenRobot;


public interface IResumenRobotService {
	
	List<ResumenRobot> findResumenRobotTopNumOrderByEspmatDesc(Integer num);
	List<ResumenRobot> findResumenRobotTopNumOrderByTotalDesc(Integer num);
	ResumenRobot findResumenRobotByRobotOrderByTotalDesc(String robot);
	ResumenRobot findResumenRobotByRobotOrderByEspmatDesc(String robot);
	List<ResumenRobot> findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByEspmatDesc(String timeframe, Long numOperaciones, Integer num);
	List<ResumenRobot> findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByTotalDesc(String timeframe, Long numOperaciones, Integer num);
}
