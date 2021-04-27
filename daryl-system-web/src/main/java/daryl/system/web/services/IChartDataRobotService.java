package daryl.system.web.services;

import java.util.List;

import daryl.system.model.DemolabOps;
import daryl.system.model.HistoricoOperaciones;
import daryl.system.web.mvc.controller.DemolabController;


public interface IChartDataRobotService {
	List<HistoricoOperaciones> findListaParaChartByRobot(String robot);
	
}
