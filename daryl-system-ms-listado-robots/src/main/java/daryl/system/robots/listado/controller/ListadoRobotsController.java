package daryl.system.robots.listado.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import daryl.system.comun.enums.Robot;
import daryl.system.model.ResumenRobot;
import daryl.system.robots.listado.controller.dto.ResumenRobotDto;
import daryl.system.robots.listado.service.IResumenRobotService;

@RestController
public class ListadoRobotsController {

	
	@Autowired
	IResumenRobotService service;
 
	@GetMapping("/")
    public List<ResumenRobotDto> main() {
		Set<ResumenRobotDto> resumenes = new TreeSet<ResumenRobotDto>();
		for (Robot robot  : Robot.values()) {
			ResumenRobot resumen = service.findResumenRobotByRobotOrderByTotalDesc(robot);
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}
		
        return new ArrayList<ResumenRobotDto>(resumenes); //view
    }
    
	
}
