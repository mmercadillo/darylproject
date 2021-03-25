package daryl.system.robots.client.web.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class DashboardController {

	@Autowired
	RestTemplate restClient;
 
	@GetMapping("/dashboard")
    public ModelAndView main() {
		
		//ModelAndView view = new ModelAndView("index");
		ModelAndView view = new ModelAndView("dashboard");
		
		view.addObject("dashboardActive", true);
		
        return view; //view
    }
    
	
}
