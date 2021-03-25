package daryl.system.robots.robotsindarwin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import daryl.system.comun.enums.Darwin;
import daryl.system.model.RobotsDarwin;
import daryl.system.robots.robotsindarwin.controller.dto.RobotInDarwinDto;
import daryl.system.robots.robotsindarwin.service.IRobotInDarwinService;

@RestController
public class RobotsInDarwinController {

	
	@Autowired
	IRobotInDarwinService service;
 
	@CrossOrigin
	@GetMapping("/darwin/{darwin}")
    public List<RobotInDarwinDto> robotPorDarwin(@PathVariable String darwin) {
		
		List<RobotsDarwin> rsInDarwin = service.findRobotsDarwinsByDarwinOrderByRobotDesc(Darwin.valueOf(darwin));
		
		Set<RobotInDarwinDto> robotsInDarwin = new TreeSet<RobotInDarwinDto>();
		for (RobotsDarwin rid : rsInDarwin) {
			if(rid != null) robotsInDarwin.add(RobotInDarwinDto.getDto(rid));
		}
		
        return new ArrayList<RobotInDarwinDto>(robotsInDarwin); //view
    }
	
	 
		@GetMapping("/robot/{robot}")
	    public List<RobotInDarwinDto> darwinsPorRobot(@PathVariable String robot) {
			
			List<RobotsDarwin> rsInDarwin = service.findRobotsDarwinsByRobotOrderByRobotDesc(robot);
			
			Set<RobotInDarwinDto> robotsInDarwin = new TreeSet<RobotInDarwinDto>();
			for (RobotsDarwin rid : rsInDarwin) {
				if(rid != null) {
					robotsInDarwin.add(RobotInDarwinDto.getDto(rid));
				}
			}
			
	        return new ArrayList<RobotInDarwinDto>(robotsInDarwin); //view
	    }
	    
    
	
}
