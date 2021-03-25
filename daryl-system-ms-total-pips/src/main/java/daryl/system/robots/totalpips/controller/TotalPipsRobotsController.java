package daryl.system.robots.totalpips.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import daryl.system.robots.totalpips.service.ITotalPipsRobotsService;

@RestController
public class TotalPipsRobotsController {

	
	@Autowired
	ITotalPipsRobotsService service;
 
	@GetMapping("/")
    public Long main() {

        return service.sumResumenRobots();
    }
    
	
}
