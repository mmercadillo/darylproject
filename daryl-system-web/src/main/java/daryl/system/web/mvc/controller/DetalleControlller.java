package daryl.system.web.mvc.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.ResumenRobot;
import daryl.system.web.mvc.dto.HistoricoParaChartDto;
import daryl.system.web.mvc.dto.ResumenRobotDto;
import daryl.system.web.mvc.dto.TotalDto;
import daryl.system.web.services.IChartDataRobotService;
import daryl.system.web.services.IDetalleRobotService;
import daryl.system.web.services.ITotalPipsRobotsService;

@RestController
public class DetalleControlller {

	@Autowired
	IChartDataRobotService charDataRobotService;
	@Autowired
	IDetalleRobotService detalleService;
	@Autowired
	ITotalPipsRobotsService totalPipsRobotsService;
 
	@GetMapping("/robot/{robot}")
    public ModelAndView main(@PathVariable String robot, Model model) {

		ModelAndView view = new ModelAndView("detalle_robot");
		
		//Datos resumen del robot
		view.addObject("bot", robot);
		List<Long> historicoParaChartDto = HistoricoParaChartDto.getDtoParaChartDeTotales(charDataRobotService.findListaParaChartByRobot(robot));
		view.addObject("datosParaChart", historicoParaChartDto);
		
		ResumenRobot resumenRobot = detalleService.findResumenRobotByRobot(robot);
		ResumenRobotDto resumenDto =  ResumenRobotDto.getDto(resumenRobot);
		view.addObject("resumenRobot",resumenDto);
		
		Long total = totalPipsRobotsService.totalPipsByRobot(robot);
		TotalDto totalDto = TotalDto.getDto(robot, total);
		view.addObject("sumRobot", totalDto.getTotal());
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Fecha primera operación
		view.addObject("fPrimeraOp", "23/11/2020 19:00");
		view.addObject("fUltimaActualizacion",resumenDto.getFModificacion());
		
		String titulo = "Detalle del robot: " + robot;
		String lugar = "Detalle robot";
		view.addObject("titulo", titulo);
		view.addObject("lugar", lugar);
		view.addObject("detalleRobotActive", true);
		

        return view; //view
    }
    
	
}
