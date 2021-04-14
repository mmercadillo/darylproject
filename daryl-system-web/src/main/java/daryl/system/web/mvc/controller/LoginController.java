package daryl.system.web.mvc.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {
 
	
	@GetMapping("/")
    public ModelAndView main(Model model) {

		ModelAndView view = new ModelAndView("login");
		
        return view;
    }
    
	
}
