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
public class TopRobotsTotalByTimeframeController {

	@Autowired
	IResumenRobotService resumenRobotService;
 
	@GetMapping("/top5Total60")
    public List<ResumenRobotDto> top5Total60() {
	
		
		//Card con el top5 en esperanza matemática con tf 60
		List<ResumenRobot> top5EspMat = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseOrderByTotalDesc("60",5);
		List<ResumenRobotDto> resumenes = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat) {
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////

		
        return resumenes;
    }
	
	@GetMapping("/top5Total240")
    public List<ResumenRobotDto> top5Total240() {
		
		//Card con el top5 en esperanza matemática con tf 60
		List<ResumenRobot> top5EspMat = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseOrderByTotalDesc("240",5);
		List<ResumenRobotDto> resumenes = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat) {
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////

		
        return resumenes;
    }
	
	@GetMapping("/top5Total1440")
    public List<ResumenRobotDto> top5Total1440() {

		
		//Card con el top5 en esperanza matemática con tf 60
		List<ResumenRobot> top5EspMat = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseOrderByTotalDesc("1440",5);
		List<ResumenRobotDto> resumenes = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat) {
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////

		
        return resumenes;
    }
	
	
	@GetMapping("/top5Total10080")
    public List<ResumenRobotDto> top5Total10080() {

		//Card con el top5 en esperanza matemática con tf 60
		List<ResumenRobot> top5EspMat = resumenRobotService.findResumenRobotsByRobotContainingIgnoreCaseOrderByTotalDesc("10080",5);
		List<ResumenRobotDto> resumenes = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5EspMat) {
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////

		
        return resumenes;
    }
	    
	
}
