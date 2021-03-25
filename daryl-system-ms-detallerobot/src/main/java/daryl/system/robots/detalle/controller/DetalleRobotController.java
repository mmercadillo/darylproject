package daryl.system.robots.detalle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import daryl.system.model.ResumenRobot;
import daryl.system.robots.detalle.controller.dto.ResumenRobotDto;
import daryl.system.robots.detalle.service.IDetalleRobotService;

@RestController
public class DetalleRobotController {

	
	@Autowired
	IDetalleRobotService service;
 
	@GetMapping("/{robot}")
    public ResumenRobotDto robotPorDarwin(@PathVariable String robot) {
		
		ResumenRobot resumenRobot = service.findResumenRobotByRobot(robot);
        return ResumenRobotDto.getDto(resumenRobot);
    }
	

    
	
}
