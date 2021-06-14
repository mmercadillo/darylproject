package daryl.system.web.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.ResumenRobot;
import daryl.system.model.Robot;
import daryl.system.model.RobotsCuenta;
import daryl.system.web.mvc.dto.ResumenRobotDto;
import daryl.system.web.mvc.dto.RobotCuentaDto;
import daryl.system.web.mvc.dto.RobotsCuentaDto;
import daryl.system.web.services.IOrdenService;
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
	@Autowired
	IOrdenService ordenService;
	
	
	@RequestMapping(path = {"/dashboard","/"}, method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView main(Model model) {

		ModelAndView view = new ModelAndView("dashboard");
		
		//Card con los robots del darwin
		List<RobotsCuenta> robotsDarwin = robotsCuentaService.findRobotsCuentaByCuenta("2100073282");
		List<RobotCuentaDto> robotsDarwinDto = new ArrayList<RobotCuentaDto>();
		for (RobotsCuenta robotCuenta : robotsDarwin) {
			if(robotCuenta != null) robotsDarwinDto.add(RobotCuentaDto.getDto(robotCuenta));
		}
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
		
		
		//Card con el top5 en resultado con tf 60
		List<ResumenRobot> top560 = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseOrderByTotalDesc("60", 5);
		List<ResumenRobotDto> resumenesTop560 = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top560) {
			if(resumen != null) resumenesTop560.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("top560", resumenesTop560);
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Card con el top5 en resultado con tf 240
		List<ResumenRobot> top5240 = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseOrderByTotalDesc("240", 5);
		List<ResumenRobotDto> resumenesTop5240 = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5240) {
			if(resumen != null) resumenesTop5240.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("top5240", resumenesTop5240);
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Card con el top5 en resultado con tf 1440
		List<ResumenRobot> top51440 = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseOrderByTotalDesc("1440", 5);
		List<ResumenRobotDto> resumenesTop51440 = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top51440) {
			if(resumen != null) resumenesTop51440.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("top51440", resumenesTop51440);
		////////////////////////////////////////////////////////////////////////////////////////////////////////	
		
		//Card con el top5 en resultado con tf 10080
		List<ResumenRobot> top510080 = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseOrderByTotalDesc("10080", 5);
		List<ResumenRobotDto> resumenesTop510080 = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top510080) {
			if(resumen != null) resumenesTop510080.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("top510080", resumenesTop510080);
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		//Card con el top5 en esperanza matemática
		List<ResumenRobot> top5EspMat = resumenRobotService.findResumenRobotTopNumOrderByEspmatDesc(5);
		List<ResumenRobotDto> resumenesTop5EspMat = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat) {
			if(resumen != null) resumenesTop5EspMat.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("top5Espmat", resumenesTop5EspMat);
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		//Card con el top5 en esperanza matemática con tf 60
		List<ResumenRobot> top5EspMat60 = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseOrderByEspmatDesc("60",5);
		List<ResumenRobotDto> resumenesTop5EspMat60 = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat60) {
			if(resumen != null) resumenesTop5EspMat60.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("top5Espmat60", resumenesTop5EspMat60);
		///////////////////////////////////////////////////////////////////////////////////////////////////////

		//Card con el top5 en esperanza matemática con tf 240
		List<ResumenRobot> top5EspMat240 = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseOrderByEspmatDesc("240",5);
		List<ResumenRobotDto> resumenesTop5EspMat240 = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat240) {
			if(resumen != null) resumenesTop5EspMat240.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("top5Espmat240", resumenesTop5EspMat240);
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Card con el top5 en esperanza matemática con tf 1440
		List<ResumenRobot> top5EspMat1440 = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseOrderByEspmatDesc("1440",5);
		List<ResumenRobotDto> resumenesTop5EspMat1440 = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat1440) {
			if(resumen != null) resumenesTop5EspMat1440.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("top5Espmat1440", resumenesTop5EspMat1440);
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Card con el top5 en esperanza matemática con tf 10080
		List<ResumenRobot> top5EspMat10080 = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseOrderByEspmatDesc("10080",5);
		List<ResumenRobotDto> resumenesTop5EspMat10080 = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat10080) {
			if(resumen != null) resumenesTop5EspMat10080.add(ResumenRobotDto.getDto(resumen));
		}
		view.addObject("top5Espmat10080", resumenesTop5EspMat10080);
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
