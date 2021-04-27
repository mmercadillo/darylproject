package daryl.system.web.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.model.ResumenRobot;
import daryl.system.model.Robot;
import daryl.system.model.RobotsCuenta;
import daryl.system.web.mvc.dto.DemolabParaChartDto;
import daryl.system.web.mvc.dto.HistoricoParaChartDto;
import daryl.system.web.mvc.dto.ResumenRobotDto;
import daryl.system.web.services.IDemolabDataService;
import daryl.system.web.services.IResumenRobotService;
import daryl.system.web.services.IRobotsCuentaService;
import daryl.system.web.services.IRobotsService;

@RestController
public class DemolabController {
 
	@Autowired
	IDemolabDataService demolabDataService;
	
	
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
	
	@RequestMapping(path = "/demolab/chart/{robot}", method = {RequestMethod.GET})
    public void chartTotal(@PathVariable String robot, HttpServletResponse response) {
		
		//Nos traemos el historico de opraciones por robot
		//EM60 - EM240 - EM1440 - EM10080
		//T60 - T240 - T1440 - T10080
		
		List<Long> historicoParaChartDto = DemolabParaChartDto.getDtoParaChartDeTotales(demolabDataService.findListaParaChartDemolabByRobot(robot.toUpperCase()));
		List<Double> periodos = new ArrayList<Double>();
		for (int i = 0; i < historicoParaChartDto.size(); i++) {
			periodos.add((double)i+1);
		}
		
		
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
		
		chart.addSeries("Pips", periodos, historicoParaChartDto);
		//BitmapEncoder.saveBitmap(chart, nombreFicheroChart.toString(), BitmapFormat.PNG);
		try {
			BitmapEncoder.saveBitmap(chart, response.getOutputStream(), BitmapFormat.PNG);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(path = "/demolab/chart/em/{robot}", method = {RequestMethod.GET})
    public void chartEm(@PathVariable String robot, HttpServletResponse response) {
		
		//Nos traemos el historico de opraciones por robot
		//EM60 - EM240 - EM1440 - EM10080
		//T60 - T240 - T1440 - T10080
		
		List<Double> historicoParaChartDto = DemolabParaChartDto.getDtoParaChartDeEspMat(demolabDataService.findListaParaChartDemolabByRobot(robot.toUpperCase()));
		List<Double> periodos = new ArrayList<Double>();
		for (int i = 0; i < historicoParaChartDto.size(); i++) {
			periodos.add((double)i+1);
		}
		
		
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
		
		chart.addSeries("Pips", periodos, historicoParaChartDto);
		//BitmapEncoder.saveBitmap(chart, nombreFicheroChart.toString(), BitmapFormat.PNG);
		try {
			BitmapEncoder.saveBitmap(chart, response.getOutputStream(), BitmapFormat.PNG);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

		
    
	
}
