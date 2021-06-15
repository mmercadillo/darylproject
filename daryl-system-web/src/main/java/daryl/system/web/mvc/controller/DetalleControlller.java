package daryl.system.web.mvc.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Orden;
import daryl.system.model.ResumenRobot;
import daryl.system.web.mvc.dto.HistoricoParaChartDto;
import daryl.system.web.mvc.dto.ResumenRobotDto;
import daryl.system.web.mvc.dto.TotalDto;
import daryl.system.web.services.IChartDataRobotService;
import daryl.system.web.services.IDetalleRobotService;
import daryl.system.web.services.IOrdenService;
import daryl.system.web.services.ITotalPipsRobotsService;

@RestController
public class DetalleControlller {

	@Autowired
	IChartDataRobotService charDataRobotService;
	@Autowired
	IDetalleRobotService detalleService;
	@Autowired
	ITotalPipsRobotsService totalPipsRobotsService;
	@Autowired
	IOrdenService ordenService;
	
	@GetMapping("/status/{robot}")
	public void status(@PathVariable String robot, HttpServletResponse response) {
		
		Orden orden =  ordenService.findByfBajaAndEstrategia(null, robot);
		if(orden != null) {
			
			String imagen = "/static/assets/admin/layout/img/remove-icon-small.png";
			if(orden.getTipoOrden() == TipoOrden.BUY) {
				imagen = "/static/assets/admin/layout/img/icon-img-up.png";
			}else if(orden.getTipoOrden() == TipoOrden.SELL) {
				imagen = "/static/assets/admin/layout/img/icon-img-down.png";
			}
		    InputStream in = getClass().getResourceAsStream(imagen);
		    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		    try {
				IOUtils.copy(in, response.getOutputStream());
			} catch (IOException e) {

			}
			
		}
		
		
	}
	
	@GetMapping("/robot/{robot}")
    public ModelAndView main(@PathVariable String robot, Model model) {

		ModelAndView view = new ModelAndView("detalle_robot");
		
		//Datos resumen del robot
		view.addObject("bot", robot);
		List<Double> historicoParaChartDto = HistoricoParaChartDto.getDtoParaChartDeTotales(charDataRobotService.findListaParaChartByRobot(robot));
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
