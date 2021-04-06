package daryl.system.robots.client.web.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.robots.client.web.mvc.dto.ResumenRobotDto;
import daryl.system.robots.client.web.mvc.dto.TotalDto;

@RestController
@RequestMapping(path = "/chart")
public class ChartControlller {

	@Autowired
	RestTemplate restClient;
 
	@GetMapping("/{robot}")
    public ModelAndView main(@PathVariable String robot, Model model) {

		ModelAndView view = new ModelAndView("chart");
		
		view.addObject("robot", robot);
		
		ResponseEntity<String> resumenes = restClient.getForEntity("http://servicio-zuul-server:8888/api/daryl/robot/chartdata/"+robot, String.class);
		System.out.println(resumenes.getBody());
		view.addObject("datosParaChart", resumenes.getBody());

		ResumenRobotDto resumenDto = restClient.getForObject("http://servicio-zuul-server:8888/api/daryl/robot/detalle/"+robot, ResumenRobotDto.class);
		view.addObject("resumenRobot",resumenDto);
		
		TotalDto totalDto = restClient.getForObject("http://servicio-zuul-server:8888/api/daryl/robot/total/" + robot, TotalDto.class);
		view.addObject("sumRobot", totalDto.getTotal());

        return view; //view
    }
    
	
}
