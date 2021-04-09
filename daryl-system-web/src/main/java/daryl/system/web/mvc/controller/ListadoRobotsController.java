package daryl.system.web.mvc.controller;

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
import daryl.system.web.mvc.dto.ResumenRobotDto;
import daryl.system.web.services.IResumenRobotService;
import daryl.system.web.services.IRobotsService;
import daryl.system.web.services.ITotalPipsRobotsService;

@RestController
public class ListadoRobotsController {
	
	@Autowired
	IResumenRobotService resumenRobotService;
	@Autowired
	IRobotsService robotsService;
	@Autowired
	ITotalPipsRobotsService totalPipsRobotsService;
	
 
	@GetMapping("/robots")
    public ModelAndView main(Model model) {

		//ModelAndView view = new ModelAndView("index");
		ModelAndView view = new ModelAndView("listado_robots");
		
		String titulo = "Listado de robots operativos";
		view.addObject("titulo", titulo);
				
		
		Set<ResumenRobotDto> resumenes = new TreeSet<ResumenRobotDto>();
		
		List<Robot> robots = robotsService.findAll();
		
		for (Robot robot  : robots) {
			ResumenRobot resumen = resumenRobotService.findResumenRobotByRobotOrderByTotalDesc(robot.getRobot());
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("resumenes", resumenes);
		
		Long sumRobots = totalPipsRobotsService.sumResumenRobots();
		view.addObject("sumRobots", sumRobots);
		
		
		view.addObject("robotsActive", true);
		
        return view; 
    }
    
	
}
