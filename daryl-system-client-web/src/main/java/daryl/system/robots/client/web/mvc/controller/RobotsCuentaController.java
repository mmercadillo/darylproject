package daryl.system.robots.client.web.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RobotsCuentaController {

	@Autowired
	RestTemplate restClient;
 
	@GetMapping("/robots/{cuenta}")
    public String main(@PathVariable String cuenta) {
		
		ModelAndView view = new ModelAndView("listado_robots");
		
		ResponseEntity<String> robots = restClient.getForEntity("http://servicio-zuul-server:8888/api/daryl/robots/cuenta/"+cuenta, String.class);
		System.out.println(robots.getBody());
		
        return robots.getBody();
    }

	
}
