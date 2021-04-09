package daryl.system.web.mvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.ResumenRobot;
import daryl.system.web.mvc.dto.HistoricoParaChartDto;
import daryl.system.web.mvc.dto.ResumenRobotDto;
import daryl.system.web.mvc.dto.TotalDto;
import daryl.system.web.services.IChartDataRobotService;
import daryl.system.web.services.IDetalleRobotService;
import daryl.system.web.services.ITotalPipsRobotsService;

@RestController
public class DetalleControlller {

	@Autowired
	IChartDataRobotService charDataRobotService;
	@Autowired
	IDetalleRobotService detalleService;
	@Autowired
	ITotalPipsRobotsService totalPipsRobotsService;
 
	@GetMapping("/robot/{robot}")
    public ModelAndView main(@PathVariable String robot, Model model) {

		ModelAndView view = new ModelAndView("detalle_robot");
		
		String titulo = "Detalle del robot: " + robot;
		view.addObject("titulo", titulo);
		view.addObject("bot", robot);
		
		/*
		ResponseEntity<String> resumenes = restClient.getForEntity("http://localhost:8888/api/daryl/robot/chartdata/"+robot, String.class);
		System.out.println(resumenes.getBody());
		view.addObject("datosParaChart", resumenes.getBody());

		ResumenRobotDto resumenDto = restClient.getForObject("http://localhost:8888/api/daryl/robot/detalle/"+robot, ResumenRobotDto.class);
		view.addObject("resumenRobot",resumenDto);
		
		TotalDto totalDto = restClient.getForObject("http://localhost:8888/api/daryl/robot/total/" + robot, TotalDto.class);
		view.addObject("sumRobot", totalDto.getTotal());
		*/
		List<Long> historicoParaChartDto = HistoricoParaChartDto.getDtoParaChart(charDataRobotService.findListaParaChartByRobot(robot));
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
