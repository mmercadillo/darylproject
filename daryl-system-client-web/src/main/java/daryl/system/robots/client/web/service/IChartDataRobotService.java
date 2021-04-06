package daryl.system.robots.client.web.service;

import java.util.List;

import daryl.system.model.HistoricoOperaciones;


public interface IChartDataRobotService {
	List<HistoricoOperaciones> findListaParaChartByRobot(String robot);
}
