package daryl.system.web.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.HistoricoOperaciones;
import daryl.system.model.Robot;
import daryl.system.web.mvc.dto.ComparadorRobotsDto;
import daryl.system.web.mvc.dto.HistoricoParaChartDto;
import daryl.system.web.mvc.dto.RobotDto;
import daryl.system.web.services.IChartDataRobotService;
import daryl.system.web.services.IRobotsService;

@RestController
public class ComparativaControlller {

	@Autowired
	IRobotsService robotsService;
	@Autowired
	IChartDataRobotService charDataRobotService;
	
	@GetMapping("/vs")
	public ModelAndView init() {
		

		ComparadorRobotsDto comparadorRobotDto = new ComparadorRobotsDto();
		ModelAndView view = new ModelAndView("vs_selector","comparadorRobotDto", comparadorRobotDto);
		
		Set<RobotDto> robotsDto = new TreeSet<RobotDto>();
		List<Robot> robots = robotsService.findAll();
		for (Robot robot  : robots) {
			if(robot != null) robotsDto.add(RobotDto.getDto(robot));
		}
		
		view.addObject("robotsDto", robotsDto);
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String titulo = "Comparador de robots";
		String lugar = "Comparador";
		view.addObject("titulo", titulo);
		view.addObject("lugar", lugar);
		view.addObject("vsActive", true);
		
        return view;
		
		
	}

	@PostMapping("/vs/compare")
	public ModelAndView compare(@ModelAttribute ComparadorRobotsDto comparadorRobotDto) {
		
		ModelAndView view = new ModelAndView("vs_selector","comparadorRobotDto", comparadorRobotDto);
		
		Set<RobotDto> robotsDto = new TreeSet<RobotDto>();
		List<Robot> robots = robotsService.findAll();
		for (Robot robot  : robots) {
			if(robot != null) robotsDto.add(RobotDto.getDto(robot));
		}
		
		view.addObject("robotsDto", robotsDto);
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		 return view;
	}
	
	@GetMapping("/vs/compare/{robotA}/{robotB}")
	public void compare(@PathVariable String robotA, @PathVariable String robotB, HttpServletResponse response) {
		
		
		if(checkRobotsForCompare(robotA, robotB) == Boolean.TRUE) {
		
			//Recuperamos la info del primer robot
			List<HistoricoOperaciones> histRobotA = charDataRobotService.findListaParaChartByRobot(robotA);
			List<Double> historicoParaChartRobotADto = HistoricoParaChartDto.getDtoParaChartDeTotales(histRobotA);
			
			
			//Recuperamos la info del segundo robot
			List<HistoricoOperaciones> histRobotB = charDataRobotService.findListaParaChartByRobot(robotB);
			List<Double> historicoParaChartRobotBDto = HistoricoParaChartDto.getDtoParaChartDeTotales(histRobotB);
			
			
			// Create Chart
			XYChart chart = new XYChartBuilder().width(1140)
												.height(470)
												//.title("Estudio")
												//.xAxisTitle("Operaciones")
												//.yAxisTitle("Puntos DAX")
												.build();
	
			// Customize Chart
			chart.getStyler().setChartTitleVisible(true);
			chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
			chart.getStyler().setYAxisLogarithmic(false);
			chart.getStyler().setXAxisLabelRotation(0);
			chart.getStyler().setXAxisTicksVisible(false);
			chart.getStyler().setMarkerSize(0);
			
			//Vemos cual tiene más operaciones
			int totalOperaciones = (historicoParaChartRobotADto.size() > historicoParaChartRobotBDto.size())?historicoParaChartRobotADto.size():historicoParaChartRobotBDto.size();
			List<Double> periodosA = new ArrayList<Double>();
			for (int i = 0; i < historicoParaChartRobotADto.size(); i++) {
				periodosA.add((double)i+1);
			}
			
			List<Double> periodosB = new ArrayList<Double>();
			for (int i = 0; i < historicoParaChartRobotBDto.size(); i++) {
				periodosB.add((double)i+1);
			}
			
			chart.addSeries(robotA, periodosA, historicoParaChartRobotADto);
			chart.addSeries(robotB, periodosB, historicoParaChartRobotBDto);
			//BitmapEncoder.saveBitmap(chart, nombreFicheroChart.toString(), BitmapFormat.PNG);
			try {
				BitmapEncoder.saveBitmap(chart, response.getOutputStream(), BitmapFormat.PNG);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	
			System.out.println("datos de comparativa recuperados");
			
		}
		
	}
	
	private Boolean checkRobotsForCompare(String robotA, String robotB) {
		
		Boolean correcto = Boolean.FALSE;
		if(robotA != null && robotB != null) {
			if(!robotA.equals("") && !robotB.equals("") ) {
				if(!robotA.equals(robotB)) {
					if(	(robotA.contains("60") && robotB.contains("60")) || (robotA.contains("240") && robotB.contains("240")) || (robotA.contains("1440") && robotB.contains("1440")) || (robotA.contains("10080") && robotB.contains("10080"))) {
						correcto = Boolean.TRUE;
					}
				}
					
			}
		}
		return correcto;
		
	}

}
