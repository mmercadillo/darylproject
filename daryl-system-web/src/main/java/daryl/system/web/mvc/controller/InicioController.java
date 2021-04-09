package daryl.system.web.mvc.controller;

import java.util.ArrayList;
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

@RestController
public class InicioController {
 
	@Autowired
	IResumenRobotService resumenRobotService;
	@Autowired
	IRobotsService robotsService;
	
	@GetMapping("/")
    public ModelAndView main(Model model) {

		ModelAndView view = new ModelAndView("index");
		

		Integer numTotalRobots = 0;
		Integer numTotalRobotsEnPositivo = 0;
		Integer numTotalRobotsEnNegativo = 0;
		
		String titulo = "Dashboard";
		
		
		List<Robot> robots = robotsService.findAll();
		List<ResumenRobot> top5 = resumenRobotService.findResumenRobotTopNumOrderByTotalDesc(5);
		
		List<ResumenRobotDto> resumenesTop5 = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5) {
			if(resumen != null) resumenesTop5.add(ResumenRobotDto.getDto(resumen));
		}
		
		for (Robot robot  : robots) {
			ResumenRobot resumen = resumenRobotService.findResumenRobotByRobotOrderByTotalDesc(robot.getRobot());
			numTotalRobots++;
			try {
				if(resumen.getTotal() > 0) numTotalRobotsEnPositivo++;
				else numTotalRobotsEnNegativo++;
			}catch (Exception e) {
				numTotalRobotsEnNegativo++;
			}
		}

		view.addObject("numTotalRobots", numTotalRobots);
		view.addObject("numTotalRobotsEnPositivo", numTotalRobotsEnPositivo);
		view.addObject("numTotalRobotsEnNegativo", numTotalRobotsEnNegativo);
		view.addObject("top5", resumenesTop5);
		
		view.addObject("titulo", titulo);
		
		view.addObject("dashboardActive", true);
		
        return view; //view
    }
    
	
}
