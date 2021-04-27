package daryl.system.web.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.ResumenRobot;
import daryl.system.model.Robot;
import daryl.system.model.RobotsCuenta;
import daryl.system.web.mvc.dto.ResumenRobotDto;
import daryl.system.web.services.IResumenRobotService;
import daryl.system.web.services.IRobotsCuentaService;
import daryl.system.web.services.IRobotsService;

@RestController
public class DemolabController {
 
	@Autowired
	IResumenRobotService resumenRobotService;
	@Autowired
	IRobotsService robotsService;
	@Autowired
	IRobotsCuentaService robotsCuentaService;
	
	
	@RequestMapping(path = "/demolab", method = {RequestMethod.GET})
    public ModelAndView main(Model model) {

		ModelAndView view = new ModelAndView("demolab");
	
		
		
		String titulo = "DemoLab";
		String lugar = "Demolab";
		
		view.addObject("titulo", titulo);
		view.addObject("lugar", lugar);
		
		view.addObject("demolabActive", true);
		
        return view; //view
    }
    
	
}
