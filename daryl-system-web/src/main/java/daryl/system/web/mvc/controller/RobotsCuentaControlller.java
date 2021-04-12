package daryl.system.web.mvc.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RobotsCuentaControlller {

 
	@GetMapping("/robots/cuenta")
    public ModelAndView getRobotsByCuenta(Model model) {

		ModelAndView view = new ModelAndView("buscador_robots_cuenta");
		
		String cuenta = "9999999";
		
		String titulo = "Robots de la cuenta: " + cuenta;
		view.addObject("titulo", titulo);
		

        return view; //view
    }
    
	
}
