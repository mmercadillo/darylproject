package daryl.system.robots.client.web.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class TopRobotsController {

	@Autowired
	RestTemplate restClient;
 
	@GetMapping("/top5")
    public String main() {
		
		ModelAndView view = new ModelAndView("listado_robots");
		
		ResponseEntity<String> robots = restClient.getForEntity("http://servicio-zuul-server:8888/api/daryl/robots/top/5", String.class);
		System.out.println(robots.getBody());
		
        return robots.getBody();
    }
	
	@GetMapping("/nbq")
    public String nbqRobots() {
		
		ModelAndView view = new ModelAndView("listado_robots");
		
		ResponseEntity<String> robots = restClient.getForEntity("http://localhost:8888/api/daryl/robots/top/3", String.class);
		System.out.println(robots.getBody());
		
        return robots.getBody();
    }
    
	
}
