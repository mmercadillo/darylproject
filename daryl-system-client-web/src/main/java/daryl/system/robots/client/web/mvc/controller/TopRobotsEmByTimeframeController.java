package daryl.system.robots.client.web.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.ResumenRobot;
import daryl.system.robots.client.web.mvc.dto.ResumenRobotDto;
import daryl.system.robots.client.web.service.IResumenRobotService;

@RestController
public class TopRobotsEmByTimeframeController {

	@Autowired
	IResumenRobotService resumenRobotService;
 
	@GetMapping("/top5Em60")
    public List<ResumenRobotDto> top5Em60() {
		
	
		
		//Card con el top5 en esperanza matemática con tf 60
		List<ResumenRobot> top5EspMat = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByEspmatDesc("60", 50L, 5);
		List<ResumenRobotDto> resumenes = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat) {
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}
		
		

		///////////////////////////////////////////////////////////////////////////////////////////////////////

		
        return resumenes;
    }
	
	@GetMapping("/top5Em240")
    public List<ResumenRobotDto> top5Em240() {

		
		//Card con el top5 en esperanza matemática con tf 60
		List<ResumenRobot> top5EspMat = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByEspmatDesc("240", 25L, 5);
		List<ResumenRobotDto> resumenes = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat) {
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////

		
        return resumenes;
    }
	
	@GetMapping("/top5Em1440")
    public List<ResumenRobotDto> top5Em1440() {
		

		//Card con el top5 en esperanza matemática con tf 60
		List<ResumenRobot> top5EspMat = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByEspmatDesc("1440", 10L, 5);
		List<ResumenRobotDto> resumenes = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat) {
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////

		
        return resumenes;
    }
	
	
	@GetMapping("/top5Em10080")
    public List<ResumenRobotDto> top5Em10080() {
		

		
		//Card con el top5 en esperanza matemática con tf 60
		List<ResumenRobot> top5EspMat = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByEspmatDesc("10080", 4L, 5);
		List<ResumenRobotDto> resumenes = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat) {
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////

		
        return resumenes;
    }
	    
	
}
