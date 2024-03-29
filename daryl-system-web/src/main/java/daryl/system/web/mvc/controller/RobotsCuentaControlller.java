package daryl.system.web.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.Robot;
import daryl.system.model.RobotsCuenta;
import daryl.system.web.mvc.dto.RobotsCuentaDto;
import daryl.system.web.services.IRobotsCuentaService;
import daryl.system.web.services.IRobotsService;

@RestController
@PreAuthorize("hasAuthority('ROLE_USER')")
public class RobotsCuentaControlller{

	@Autowired
	IRobotsService robotsService;
	@Autowired
	IRobotsCuentaService robotsCuentaService;
	
	//@PostMapping("/robots/cuenta")
	@RequestMapping(value = "/robots/cuenta", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getRobots() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		RobotsCuentaDto rcdto = new RobotsCuentaDto();
			rcdto.setCuenta( ((UserDetails)authentication.getPrincipal()).getUsername());
		ModelAndView view = new ModelAndView("buscador_robots_cuenta", "robots_cuenta", rcdto);
		
		
		String titulo = "Buscador robots por cuenta ";
		String lugar = "Robots Cuenta";
		view.addObject("titulo", titulo);
		view.addObject("lugar", lugar);
		view.addObject("RobotsCuentaActive", true);

        return view; //view
    }
	
	@PostMapping("/robots/cuenta/find")
    public ModelAndView getRobotsByCuenta(@ModelAttribute RobotsCuentaDto rcdto) {

		ModelAndView view = new ModelAndView("robots_cuenta", "robots_cuenta", rcdto);
		
		String cuenta = rcdto.getCuenta();
		
		//Robots asociados a la cuenta
		List<RobotsCuenta> robotsCuenta = robotsCuentaService.findRobotsCuentaByCuenta(cuenta);
		List<String> robotsAsociadosCuenta = new ArrayList<String>();
		if(robotsCuenta != null) {
			for (RobotsCuenta rc : robotsCuenta) {
				robotsAsociadosCuenta.add(rc.getRobot());
			}
		}
		rcdto.setRobots(robotsAsociadosCuenta);
		view.addObject("robotsAsociadosCuenta", robotsAsociadosCuenta);
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Listado de robots para generar los checks
		List<Robot> robots = robotsService.findAllByOrderByRobotAsc();
		view.addObject("robots", robots);
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String titulo = "Robots asignados a la cuenta: " + cuenta;
		String lugar = "Robots Cuenta";
		view.addObject("titulo", titulo);
		view.addObject("lugar", lugar);
		view.addObject("RobotsCuentaActive", true);
		

        return view; //view
    }
	
	@PostMapping("/robots/cuenta/alta")
    public ModelAndView saveRobotsByCuenta(@ModelAttribute RobotsCuentaDto rcdto) {

		
		String cuenta = rcdto.getCuenta();
		
		//Borramos los anteriores
		List<RobotsCuenta> robotsCuenta = robotsCuentaService.findRobotsCuentaByCuenta(cuenta);
		if(robotsCuenta != null && robotsCuenta .size() > 0) robotsCuentaService.deleteAll(robotsCuenta);
		if(rcdto.getRobots() != null) {
			robotsCuenta = new ArrayList<RobotsCuenta>();
			Long currentTime = System.currentTimeMillis();
			for (String  robot : rcdto.getRobots()) {
				RobotsCuenta rc = new RobotsCuenta();
					rc.setCuenta(cuenta);
					rc.setRobot(robot);
					rc.setFAlta(currentTime);
				robotsCuenta.add(rc);
			}
			robotsCuentaService.saveAll(robotsCuenta);
		}
		
		

        return getRobotsByCuenta(rcdto); //view
    }
	
}
