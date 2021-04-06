package daryl.system.robots.client.web.mvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.ResumenRobot;
import daryl.system.robots.client.web.mvc.dto.HistoricoParaChartDto;
import daryl.system.robots.client.web.mvc.dto.ResumenRobotDto;
import daryl.system.robots.client.web.mvc.dto.TotalDto;
import daryl.system.robots.client.web.service.IChartDataRobotService;
import daryl.system.robots.client.web.service.IDetalleRobotService;
import daryl.system.robots.client.web.service.ITotalPipsRobotsService;

@RestController
@RequestMapping(path = "/chart")
public class ChartControlller {

	@Autowired
	IChartDataRobotService service;
	@Autowired
	IDetalleRobotService detalleService;
	@Autowired
	ITotalPipsRobotsService totalPipsRobotsService;
 
	@GetMapping("/{robot}")
    public ModelAndView main(@PathVariable String robot, Model model) {

		ModelAndView view = new ModelAndView("chart");
		
		view.addObject("robot", robot);
		
		/*
		ResponseEntity<String> resumenes = restClient.getForEntity("http://localhost:8888/api/daryl/robot/chartdata/"+robot, String.class);
		System.out.println(resumenes.getBody());
		view.addObject("datosParaChart", resumenes.getBody());

		ResumenRobotDto resumenDto = restClient.getForObject("http://localhost:8888/api/daryl/robot/detalle/"+robot, ResumenRobotDto.class);
		view.addObject("resumenRobot",resumenDto);
		
		TotalDto totalDto = restClient.getForObject("http://localhost:8888/api/daryl/robot/total/" + robot, TotalDto.class);
		view.addObject("sumRobot", totalDto.getTotal());
		*/
		List<Long> historicoParaChartDto = HistoricoParaChartDto.getDtoParaChart(service.findListaParaChartByRobot(robot));
		view.addObject("datosParaChart", historicoParaChartDto);
		
		ResumenRobot resumenRobot = detalleService.findResumenRobotByRobot(robot);
		ResumenRobotDto resumenDto =  ResumenRobotDto.getDto(resumenRobot);
		view.addObject("resumenRobot",resumenDto);
		
		Long total = totalPipsRobotsService.totalPipsByRobot(robot);
		TotalDto totalDto = TotalDto.getDto(robot, total);
		view.addObject("sumRobot", totalDto.getTotal());

        return view; //view
    }
    
	
}
