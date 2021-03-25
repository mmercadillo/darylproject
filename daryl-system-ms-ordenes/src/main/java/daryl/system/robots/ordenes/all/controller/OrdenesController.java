package daryl.system.robots.ordenes.all.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import daryl.system.model.Orden;
import daryl.system.model.ResumenRobot;
import daryl.system.model.RobotsCuenta;
import daryl.system.robots.ordenes.all.controller.dto.OrdenDto;
import daryl.system.robots.ordenes.all.service.IOrdenesService;
import daryl.system.robots.ordenes.all.service.IResumenRobotService;
import daryl.system.robots.ordenes.all.service.IRobotsCuentaService;

@RestController
public class OrdenesController {

	
	@Autowired
	IOrdenesService service;
	@Autowired
	IResumenRobotService resumenService;
	@Autowired
	IRobotsCuentaService robotsCuentaService;
	
	
	@GetMapping("/all")
    public List<OrdenDto> main() {
		
		List<Orden> ordenes = service.findAllByOrderByRobotAsc();
		
		List<OrdenDto> ordenesDto = new ArrayList<OrdenDto>();
		for (Orden orden : ordenes) {
			if(orden != null) ordenesDto.add(OrdenDto.getDto(orden));
		}
		
		
		return ordenesDto;
    }
	
	@GetMapping("/top/{num}")
    public List<OrdenDto> main(@PathVariable Integer num) {
		
		//Recuperamos el top "num" de los robots
		List<ResumenRobot> topNumRobots = resumenService.findResumenRobotTopNumOrderByTotalDesc(num);
		
		List<String> robots = new ArrayList<String>();
		//Recuperamos la orden por robot
		if(topNumRobots != null) {
			for (ResumenRobot resumen : topNumRobots) {
				robots.add(resumen.getRobot());
			}
		}
		
		List<Orden> ordenes = service.findAllByRobots(robots);
		
		List<OrdenDto> ordenesDto = new ArrayList<OrdenDto>();
		for (Orden orden : ordenes) {
			if(orden != null) ordenesDto.add(OrdenDto.getDto(orden));
		}
		
		
		return ordenesDto;
    }
	
	@GetMapping("/cuenta/{cuenta}")
    public List<OrdenDto> main(@PathVariable String cuenta) {
		
		//Recuperamos los tobots de la Cuenta
		List<RobotsCuenta> robotsCuenta = robotsCuentaService.findRobotsCuentaByCuenta(cuenta);
		System.out.println(robotsCuenta);
		
		List<String> robots = new ArrayList<String>();
		//Recuperamos la orden por robot
		if(robotsCuenta != null) {
			for (RobotsCuenta robotCuenta : robotsCuenta) {
				robots.add(robotCuenta.getRobot());
			}
		}
		System.out.println(robots);
		List<Orden> ordenes = service.findAllByRobots(robots);
		System.out.println(ordenes);
		List<OrdenDto> ordenesDto = new ArrayList<OrdenDto>();
		for (Orden orden : ordenes) {
			if(orden != null) ordenesDto.add(OrdenDto.getDto(orden));
		}
		
		System.out.println(ordenesDto);
		return ordenesDto;
    }
    
    
	
}
