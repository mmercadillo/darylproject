package daryl.system.web.mvc.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class InicioController {
 
	@GetMapping("/")
    public ModelAndView main(Model model) {

		/*
		ModelAndView view = new ModelAndView("login/login-4");		
        return view; //view
        */
		
		//ModelAndView view = new ModelAndView("index");
		ModelAndView view = new ModelAndView("index");
		
		Integer numTotalRobots = 10;
		Integer numTotalRobotsEnPositivo = 10;
		Integer numTotalRobotsEnNegativo = 10;
		
		String titulo = "Dashboard";
		
		view.addObject("numTotalRobots", numTotalRobots);
		view.addObject("numTotalRobotsEnPositivo", numTotalRobotsEnPositivo);
		view.addObject("numTotalRobotsEnNegativo", numTotalRobotsEnNegativo);
		
		view.addObject("titulo", titulo);
		
		
		
		view.addObject("dashboardActive", true);
		
        return view; //view
    }
    
	
}
