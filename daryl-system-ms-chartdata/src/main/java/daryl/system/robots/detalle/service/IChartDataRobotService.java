package daryl.system.robots.detalle.service;

import java.util.List;

import daryl.system.model.HistoricoOperaciones;


public interface IChartDataRobotService {
	List<HistoricoOperaciones> findListaParaChartByRobot(String robot);
}
