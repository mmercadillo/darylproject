package daryl.system.robots.client.web.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ListadoRobotsController {

	@Autowired
	RestTemplate restClient;
 
	@GetMapping("/robots")
    public ModelAndView main(Model model) {
		
		ModelAndView view = new ModelAndView("listado_robots");
		
		ResponseEntity<String> resumenes = restClient.getForEntity("http://localhost:8888/api/daryl/robots/listado", String.class);
		System.out.println(resumenes);
		view.addObject("robots", resumenes.getBody());
		
		ResponseEntity<Long> total = restClient.getForEntity("http://localhost:8888/api/daryl/robots/total", Long.class);
		view.addObject("sumRobots", total.getBody());
		
		view.addObject("robotsActive", true);
		
        return view;
    }
    
	
}
