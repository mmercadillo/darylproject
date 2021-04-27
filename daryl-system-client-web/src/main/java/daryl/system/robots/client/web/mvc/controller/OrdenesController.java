package daryl.system.robots.client.web.mvc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import daryl.system.model.Orden;
import daryl.system.model.ResumenRobot;
import daryl.system.model.RobotsCuenta;
import daryl.system.robots.client.web.mvc.dto.OrdenDto;
import daryl.system.robots.client.web.service.IOrdenesService;
import daryl.system.robots.client.web.service.IResumenRobotService;
import daryl.system.robots.client.web.service.IRobotsCuentaService;

@RestController
public class OrdenesController {

	@Autowired
	IOrdenesService service;
	@Autowired
	IResumenRobotService resumenService;
	@Autowired
	IRobotsCuentaService robotsCuentaService;
 
	@GetMapping("/mt/ordenes/all/{cta}")
    public List<OrdenDto> main( @PathVariable String cta) {
		
		System.out.println("FULL: " + cta + " " + new Date());

		
		List<Orden> ordenes = service.findAllByOrderByRobotAsc();
		
		List<OrdenDto> ordenesDto = new ArrayList<OrdenDto>();
		for (Orden orden : ordenes) {
			if(orden != null) ordenesDto.add(OrdenDto.getDto(orden));
		}
		
		
		return ordenesDto;
    }
    
	@GetMapping("/mt/ordenes/{num}/{cta}")
    public List<OrdenDto> main(@PathVariable Integer num, @PathVariable String cta) {
		System.out.println("TOP: " + cta + " NUM: " + num + " - " + new Date());
		

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
	
	@GetMapping("/mt/ordenes/cuenta/{cta}")
    public List<OrdenDto> ordenesCuenta(@PathVariable String cta) {
		System.out.println("ROBOTS CTA: " + cta + new Date());
		

		
		System.out.println("FULL: " + cta + " " + new Date());
		
		//Recuperamos los tobots de la Cuenta
		List<RobotsCuenta> robotsCuenta = robotsCuentaService.findRobotsCuentaByCuenta(cta);
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

        return ordenesDto;
		
		
		//return ordenes;
    }
	
	
	
}
