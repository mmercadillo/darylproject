package daryl.system.web.mvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.backtest.ResumenRobotBacktest;
import daryl.system.web.mvc.dto.HistoricoParaChartDto;
import daryl.system.web.mvc.dto.ResumenRobotBacktestDto;
import daryl.system.web.mvc.dto.ResumenRobotDto;
import daryl.system.web.mvc.dto.TotalDto;
import daryl.system.web.services.IChartDataRobotService;
import daryl.system.web.services.IDetalleBacktestRobotService;
import daryl.system.web.services.ITotalPipsRobotsService;

@RestController
public class DetalleBacktestRobotControlller {

	@Autowired
	IChartDataRobotService charDataRobotService;
	@Autowired
	IDetalleBacktestRobotService detalleBacktestRobotService;
	@Autowired
	ITotalPipsRobotsService totalPipsRobotsService;

	@GetMapping("/backtest")
    public ModelAndView init() {
		
		ModelAndView view = new ModelAndView("detalle_backtest_robot");
		
		return view;
		
	}
	
	@GetMapping("/backtest/{robot}")
    public ModelAndView main(@PathVariable String robot, Model model) {

		ModelAndView view = new ModelAndView("detalle_backtest_robot");
		
		//Datos resumen del robot
		view.addObject("bot", robot);
		List<Long> historicoParaChartDto = HistoricoParaChartDto.getDtoParaChartDeTotales(charDataRobotService.findListaParaChartByRobot(robot));
		view.addObject("datosParaChart", historicoParaChartDto);
		
		ResumenRobotBacktest resumenBacktestRobot = detalleBacktestRobotService.findResumenRobotBacktestByRobot(robot);
		ResumenRobotBacktestDto resumenBacktestDto =  ResumenRobotBacktestDto.getDto(resumenBacktestRobot);
		view.addObject("resumenRobotBacktest",resumenBacktestDto);
		
		Long total = totalPipsRobotsService.totalPipsByRobot(robot);
		TotalDto totalDto = TotalDto.getDto(robot, total);
		view.addObject("sumRobot", totalDto.getTotal());
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Fecha primera operación
		view.addObject("fPrimeraOp", resumenBacktestDto.getFprimeraOpTxt());
		view.addObject("fUltimaActualizacion",resumenBacktestDto.getFultimaOpTxt());
		
		String titulo = "Backtest del robot: " + robot;
		String lugar = "Backtest robot";
		view.addObject("titulo", titulo);
		view.addObject("lugar", lugar);
		view.addObject("detalleRobotBacktestActive", true);
		

        return view; //view
    }
    
	
}
