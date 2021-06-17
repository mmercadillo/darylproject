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
public class OrdenesController2 {

	@Autowired
	IOrdenesService service;
	@Autowired
	IResumenRobotService resumenService;
	@Autowired
	IRobotsCuentaService robotsCuentaService;
 

	
	@GetMapping("/mt/ordenes/em/tf60/{cta}")
    public List<OrdenDto> ordenesEmTf60(@PathVariable String cta) {

		List<ResumenRobot> top5EspMat = resumenService.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByEspmatDesc("60", 50, 10);
		List<String> robots = new ArrayList<String>();
		//Recuperamos la orden por robot
		if(top5EspMat != null) {
			for (ResumenRobot resumen : top5EspMat) {
				robots.add(resumen.getRobot());
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
	
	
	@GetMapping("/mt/ordenes/em/tf240/{cta}")
    public List<OrdenDto> ordenesEmTf240(@PathVariable String cta) {

		List<ResumenRobot> top5EspMat = resumenService.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByEspmatDesc("240", 25, 10);
		List<String> robots = new ArrayList<String>();
		//Recuperamos la orden por robot
		if(top5EspMat != null) {
			for (ResumenRobot resumen : top5EspMat) {
				robots.add(resumen.getRobot());
			}
		}
		System.out.println("EmTf240 --->>>>>> " + robots);
		List<Orden> ordenes = service.findAllByRobots(robots);
		System.out.println("EmTf240 --->>>>>> " + ordenes);
		List<OrdenDto> ordenesDto = new ArrayList<OrdenDto>();
		for (Orden orden : ordenes) {
			if(orden != null) ordenesDto.add(OrdenDto.getDto(orden));
		}

        return ordenesDto;
		
		
		//return ordenes;
    }
	
	
	@GetMapping("/mt/ordenes/em/tf1440/{cta}")
    public List<OrdenDto> ordenesEmTf1440(@PathVariable String cta) {

		List<ResumenRobot> top5EspMat = resumenService.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByEspmatDesc("1440", 10, 5);
		List<String> robots = new ArrayList<String>();
		//Recuperamos la orden por robot
		if(top5EspMat != null) {
			for (ResumenRobot resumen : top5EspMat) {
				robots.add(resumen.getRobot());
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
	
	
	@GetMapping("/mt/ordenes/em/tf10080/{cta}")
    public List<OrdenDto> ordenesEmTf10080(@PathVariable String cta) {

		List<ResumenRobot> top5EspMat = resumenService.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByEspmatDesc("10080", 4, 5);
		List<String> robots = new ArrayList<String>();
		//Recuperamos la orden por robot
		if(top5EspMat != null) {
			for (ResumenRobot resumen : top5EspMat) {
				robots.add(resumen.getRobot());
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

	@GetMapping("/mt/ordenes/total/tf60/{cta}")
    public List<OrdenDto> ordenesTotalTf60(@PathVariable String cta) {

		List<ResumenRobot> top5EspMat = resumenService.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByTotalDesc("60", 50, 10);
		List<String> robots = new ArrayList<String>();
		//Recuperamos la orden por robot
		if(top5EspMat != null) {
			for (ResumenRobot resumen : top5EspMat) {
				robots.add(resumen.getRobot());
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
	
	@GetMapping("/mt/ordenes/total/tf240/{cta}")
    public List<OrdenDto> ordenesTotalTf240(@PathVariable String cta) {

		List<ResumenRobot> top5EspMat = resumenService.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByTotalDesc("240", 25, 10);
		List<String> robots = new ArrayList<String>();
		//Recuperamos la orden por robot
		if(top5EspMat != null) {
			for (ResumenRobot resumen : top5EspMat) {
				robots.add(resumen.getRobot());
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
	
	@GetMapping("/mt/ordenes/total/tf1440/{cta}")
    public List<OrdenDto> ordenesTotalTf1440(@PathVariable String cta) {

		List<ResumenRobot> top5EspMat = resumenService.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByTotalDesc("1440", 10, 5);
		List<String> robots = new ArrayList<String>();
		//Recuperamos la orden por robot
		if(top5EspMat != null) {
			for (ResumenRobot resumen : top5EspMat) {
				robots.add(resumen.getRobot());
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
	
	@GetMapping("/mt/ordenes/total/tf10080/{cta}")
    public List<OrdenDto> ordenesTotalTf10080(@PathVariable String cta) {

		List<ResumenRobot> top5EspMat = resumenService.findResumenRobotsByRobotContainingIgnoreCaseAndNumOperacionesGreaterThanEqualOrderByTotalDesc("10080", 4, 5);
		List<String> robots = new ArrayList<String>();
		//Recuperamos la orden por robot
		
		
		
		if(top5EspMat != null) {
			for (ResumenRobot resumen : top5EspMat) {
				robots.add(resumen.getRobot());
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
