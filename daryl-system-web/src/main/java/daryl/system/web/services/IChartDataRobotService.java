package daryl.system.web.services;

import java.util.List;

import daryl.system.model.HistoricoOperaciones;


public interface IChartDataRobotService {
	List<HistoricoOperaciones> findListaParaChartByRobot(String robot);
}
