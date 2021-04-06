package daryl.system.robots.client.web.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.RobotsCuenta;
import daryl.system.robots.client.web.mvc.dto.RobotsCuentaDto;
import daryl.system.robots.client.web.service.IRobotsCuentaService;

@RestController
public class RobotsCuentaController {

	
	@Autowired
	IRobotsCuentaService service;
 
	@GetMapping("/robots/{cuenta}")
    public List<RobotsCuentaDto> main(@PathVariable String cuenta) {
		
		ModelAndView view = new ModelAndView("listado_robots");
		/*
		ResponseEntity<String> robots = restClient.getForEntity("http://localhost:8888/api/daryl/robots/cuenta/"+cuenta, String.class);
		System.out.println(robots.getBody());
		*/
		
		List<RobotsCuenta> robotsCuenta = service.findRobotsCuentaByCuenta(cuenta);
		
		List<RobotsCuentaDto> robots = new ArrayList<RobotsCuentaDto>();
		for (RobotsCuenta robotCuenta : robotsCuenta) {
			if(robots != null) robots.add(RobotsCuentaDto.getDto(robotCuenta));
		}
		
		
        return robots;
    }

	
}
