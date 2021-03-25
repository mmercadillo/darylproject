package daryl.system.robots.detalle.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import daryl.system.robots.detalle.controller.dto.HistoricoParaChartDto;
import daryl.system.robots.detalle.service.IChartDataRobotService;

@RestController
public class ChartDataRobotController {

	
	@Autowired
	IChartDataRobotService service;
 
	@GetMapping("/{robot}")
    public List<Long> robotPorDarwin(@PathVariable String robot) {
		return HistoricoParaChartDto.getDtoParaChart(service.findListaParaChartByRobot(robot));
    }
	

    
	
}
