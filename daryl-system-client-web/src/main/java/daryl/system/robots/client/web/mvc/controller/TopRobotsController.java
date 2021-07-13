package daryl.system.robots.client.web.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.ResumenRobot;
import daryl.system.robots.client.web.mvc.dto.ResumenRobotDto;
import daryl.system.robots.client.web.service.IResumenRobotService;

@RestController
public class TopRobotsController {

	@Autowired
	IResumenRobotService service;
 
	@GetMapping("/top5")
    public List<ResumenRobotDto> main() {
		
		ModelAndView view = new ModelAndView("listado_robots");
		/*
		ResponseEntity<String> robots = restClient.getForEntity("http://localhost:8888/api/daryl/robots/top/5", String.class);
		System.out.println(robots.getBody());
		*/
		
		List<ResumenRobot> top5 = service.findResumenRobotTopNumOrderByTotalDesc(5);
		
		List<ResumenRobotDto> resumenes = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5) {
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}
		
        return resumenes;
    }
	
	@GetMapping("/nbq")
    public List<ResumenRobotDto> nbqRobots() {
		
		ModelAndView view = new ModelAndView("listado_robots");
		/*
		ResponseEntity<String> robots = restClient.getForEntity("http://localhost:8888/api/daryl/robots/top/3", String.class);
		System.out.println(robots.getBody());
		*/
		
		List<ResumenRobot> top5 = service.findResumenRobotTopNumOrderByTotalDesc(3);
		
		List<ResumenRobotDto> resumenes = new ArrayList<ResumenRobotDto>();
		for (ResumenRobot resumen : top5) {
			if(resumen != null) resumenes.add(ResumenRobotDto.getDto(resumen));
		}
		
        return resumenes;
		
    }
    
	
}
