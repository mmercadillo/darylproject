package daryl.system.web.services;

import java.util.List;

import daryl.system.model.DemolabOps;
import daryl.system.model.HistoricoOperaciones;
import daryl.system.web.mvc.controller.DemolabController;


public interface IDemolabDataService {
	List<DemolabOps> findListaParaChartDemolabByRobot(String robot);
	List<DemolabOps> findAllByOrderByFcierre();
}
