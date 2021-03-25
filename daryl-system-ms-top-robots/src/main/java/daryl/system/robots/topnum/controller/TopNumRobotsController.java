package daryl.system.robots.topnum.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import daryl.system.model.ResumenRobot;
import daryl.system.robots.topnum.controller.dto.ResumenRobotDto;
import daryl.system.robots.topnum.service.IResumenRobotService;

@RestController
public class TopNumRobotsController {

	
	@Autowired
	IResumenRobotService service;
 
	@GetMapping("/{num}")
    public List<ResumenRobotDto> main(@PathVariable Integer num) {
		
		List<ResumenRobot> top5 = service.findResumenRobotTopNumOrderByTotalDesc(num);
		
		List<ResumenRobotDto> resumenes = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5) {
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}
		
        return resumenes; //view
    }
    
	
}
