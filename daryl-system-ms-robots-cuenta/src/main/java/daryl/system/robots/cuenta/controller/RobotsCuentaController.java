package daryl.system.robots.cuenta.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import daryl.system.model.RobotsCuenta;
import daryl.system.robots.cuenta.controller.dto.RobotsCuentaDto;
import daryl.system.robots.cuenta.service.IRobotsCuentaService;

@RestController
public class RobotsCuentaController {

	
	@Autowired
	IRobotsCuentaService service;
 
	@GetMapping("/robots/cuenta/{cuenta}")
    public List<RobotsCuentaDto> main(@PathVariable String cuenta) {
		
		List<RobotsCuenta> robotsCuenta = service.findRobotsCuentaByCuenta(cuenta);
		
		List<RobotsCuentaDto> robots = new ArrayList<RobotsCuentaDto>();
		for (RobotsCuenta robotCuenta : robotsCuenta) {
			if(robots != null) robots.add(RobotsCuentaDto.getDto(robotCuenta));
		}
		
        return robots; //view
    }
    
	
}
