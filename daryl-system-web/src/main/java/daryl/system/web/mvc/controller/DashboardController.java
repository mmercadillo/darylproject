package daryl.system.web.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.ResumenRobot;
import daryl.system.model.Robot;
import daryl.system.model.RobotsCuenta;
import daryl.system.web.mvc.dto.ResumenRobotDto;
import daryl.system.web.services.IResumenRobotService;
import daryl.system.web.services.IRobotsCuentaService;
import daryl.system.web.services.IRobotsService;

@RestController
public class DashboardController {
 
	@Autowired
	IResumenRobotService resumenRobotService;
	@Autowired
	IRobotsService robotsService;
	@Autowired
	IRobotsCuentaService robotsCuentaService;
	
	
	@RequestMapping(path = "/dashboard", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView main(Model model) {

		ModelAndView view = new ModelAndView("dashboard");
		
		//Card con los robots del darwin
		List<RobotsCuenta> robotsDarwin = robotsCuentaService.findRobotsCuentaByCuenta("2100073282");
		view.addObject("robotsDarwin", robotsDarwin);
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Card con el top5 en resultado
		List<ResumenRobot> top5 = resumenRobotService.findResumenRobotTopNumOrderByTotalDesc(5);
		List<ResumenRobotDto> resumenesTop5 = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5) {
			if(resumen != null) resumenesTop5.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("top5", resumenesTop5);
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Card con el top5 en esperanza matemática
		List<ResumenRobot> top5EspMat = resumenRobotService.findResumenRobotTopNumOrderByEspmatDesc(5);
		List<ResumenRobotDto> resumenesTop5EspMat = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat) {
			if(resumen != null) resumenesTop5EspMat.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("top5Espmat", resumenesTop5EspMat);
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		//Robots para calcular los cards con los resúmenes resumenes
		Integer numTotalRobots = 0;
		Integer numTotalRobotsEnPositivo = 0;
		Integer numTotalRobotsEnNegativo = 0;
		List<Robot> robots = robotsService.findAll();
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
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		String titulo = "Dashboard";
		String lugar = "Dashboard";
		
		view.addObject("titulo", titulo);
		view.addObject("lugar", lugar);
		
		view.addObject("dashboardActive", true);
		
        return view; //view
    }
    
	
}
