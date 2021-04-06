package daryl.system.robots.client.web.mvc.controller;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.ResumenRobot;
import daryl.system.model.Robot;
import daryl.system.robots.client.web.mvc.dto.ResumenRobotDto;
import daryl.system.robots.client.web.service.IResumenRobotService;
import daryl.system.robots.client.web.service.IRobotService;
import daryl.system.robots.client.web.service.ITotalPipsRobotsService;

@RestController
public class ListadoRobotsController {

	
	@Autowired
	IResumenRobotService service;
	@Autowired
	IRobotService robotService;
	@Autowired
	ITotalPipsRobotsService totalPipsRobotsService;
 
	@GetMapping("/robots")
    public ModelAndView main(Model model) {
		
		ModelAndView view = new ModelAndView("listado_robots");
		
		/*
		ResponseEntity<String> resumenes = restClient.getForEntity("http://localhost:8888/api/daryl/robots/listado", String.class);
		System.out.println(resumenes);
		view.addObject("robots", resumenes.getBody());
		
		ResponseEntity<Long> total = restClient.getForEntity("http://localhost:8888/api/daryl/robots/total", Long.class);
		view.addObject("sumRobots", total.getBody());
		
		view.addObject("robotsActive", true);
		*/
		
		Set<ResumenRobotDto> resumenes = new TreeSet<ResumenRobotDto>();
		
		List<Robot> robots = robotService.findAll();
		
		for (Robot robot  : robots) {
			ResumenRobot resumen = service.findResumenRobotByRobotOrderByTotalDesc(robot.getRobot());
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("robots", resumenes);
		
		Long sumRobots = totalPipsRobotsService.sumResumenRobots();
		view.addObject("sumRobots", sumRobots);
		
		view.addObject("robotsActive", true);
		
        return view;
    }
    
	
}
