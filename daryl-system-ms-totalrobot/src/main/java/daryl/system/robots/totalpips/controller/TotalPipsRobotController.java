package daryl.system.robots.totalpips.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import daryl.system.robots.totalpips.controller.dto.TotalDto;
import daryl.system.robots.totalpips.service.ITotalPipsRobotService;

@RestController
public class TotalPipsRobotController {

	
	@Autowired
	ITotalPipsRobotService service;
 
	@GetMapping("/{robot}")
    public TotalDto main(@PathVariable String robot) {
		
		Long total = service.totalPipsByRobot(robot);
		
        return TotalDto.getDto(robot, total);
    }
    
	
}
