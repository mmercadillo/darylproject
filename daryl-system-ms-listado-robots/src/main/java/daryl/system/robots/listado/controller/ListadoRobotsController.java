package daryl.system.robots.listado.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import daryl.system.model.ResumenRobot;
import daryl.system.model.Robot;
import daryl.system.robots.listado.controller.dto.ResumenRobotDto;
import daryl.system.robots.listado.service.IResumenRobotService;
import daryl.system.robots.listado.service.IRobotService;

@RestController
public class ListadoRobotsController {

	
	@Autowired
	IResumenRobotService service;
	@Autowired
	IRobotService robotService;
	
	@GetMapping("/")
    public List<ResumenRobotDto> main() {
		Set<ResumenRobotDto> resumenes = new TreeSet<ResumenRobotDto>();
		
		List<Robot> robots = robotService.findAll();
		
		for (Robot robot  : robots) {
			ResumenRobot resumen = service.findResumenRobotByRobotOrderByTotalDesc(robot.getRobot());
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}
		
        return new ArrayList<ResumenRobotDto>(resumenes); //view
    }
    
	
}
