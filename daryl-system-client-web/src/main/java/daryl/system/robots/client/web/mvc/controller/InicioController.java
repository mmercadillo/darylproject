package daryl.system.robots.client.web.mvc.controller;

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
		ModelAndView view = new ModelAndView("dashboard");
		
		view.addObject("dashboardActive", true);
		
        return view; //view
    }
    
	
}
