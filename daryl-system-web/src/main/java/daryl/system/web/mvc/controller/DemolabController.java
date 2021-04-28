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

import daryl.system.comun.enums.DemolabRobot;
import daryl.system.model.ResumenRobot;
import daryl.system.model.ResumenRobotDemolab;
import daryl.system.model.Robot;
import daryl.system.model.RobotsCuenta;
import daryl.system.web.mvc.dto.DemolabParaChartDto;
import daryl.system.web.mvc.dto.HistoricoParaChartDto;
import daryl.system.web.mvc.dto.ResumenRobotDto;
import daryl.system.web.services.IDemolabDataService;
import daryl.system.web.services.IDetalleDemolabRobotService;
import daryl.system.web.services.IDetalleRobotService;
import daryl.system.web.services.IResumenRobotService;
import daryl.system.web.services.IRobotsCuentaService;
import daryl.system.web.services.IRobotsService;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.ResubmissionScheduler;

@RestController
public class DemolabController {
 
	@Autowired
	IDemolabDataService demolabDataService;
	@Autowired
	IDetalleDemolabRobotService detalleRobotDemolabService;
	
	@RequestMapping(path = "/demolab", method = {RequestMethod.GET})
    public ModelAndView main(Model model) {

		ModelAndView view = new ModelAndView("demolab");
	
		
		for(DemolabRobot robot : DemolabRobot.values()) {

			
			String varTotal = robot.name() + "_Total";
			String varEM = robot.name() + "_Em";
			String varOps = robot.name() + "_Ops";
			String varOpsW = robot.name() + "_OpsW";
			String varOpsL = robot.name() + "_OpsL";
			String varPctOpsW = robot.name() + "_PctOpsW";
			String varPctOpsL = robot.name() + "_PctOpsL";
			String varAvgW = robot.name() + "_AvgW";
			String varAvgL = robot.name() + "_AvgL";
			
			try {
				ResumenRobotDemolab resumen = detalleRobotDemolabService.findResumenRobotDemolabByRobot(robot.name());

				view.addObject(varTotal, resumen.getTotal());
				view.addObject(varEM, resumen.getEspmat());
				view.addObject(varOps, resumen.getNumOperaciones());
				view.addObject(varOpsW, resumen.getNumOpsGanadoras());
				view.addObject(varOpsL, resumen.getNumOpsPerdedoras());
				view.addObject(varPctOpsW, resumen.getPctOpsGanadoras());
				view.addObject(varPctOpsL, resumen.getPctOpsPerdedoras());
				view.addObject(varAvgW, resumen.getGananciaMediaPorOpGanadora());
				view.addObject(varAvgL, resumen.getPerdidaMediaPorOpPerdedora());
				
				
			}catch (Exception e) {
				
				view.addObject(varTotal, 0);
				view.addObject(varEM, 0);
				view.addObject(varOps, 0);
				view.addObject(varOpsW, 0);
				view.addObject(varOpsL, 0);
				view.addObject(varPctOpsW, 0);
				view.addObject(varPctOpsL, 0);
				view.addObject(varAvgW, 0);
				view.addObject(varAvgL, 0);
				
			}
			
			
		}

		
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
		

		try {
			chart.addSeries("Pips", periodos, historicoParaChartDto);
			//BitmapEncoder.saveBitmap(chart, nombreFicheroChart.toString(), BitmapFormat.PNG);
			BitmapEncoder.saveBitmap(chart, response.getOutputStream(), BitmapFormat.PNG);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}
	
	@RequestMapping(path = "/demolab/chart/em/{robot}", method = {RequestMethod.GET})
    public void chartEm(@PathVariable String robot, HttpServletResponse response) {
		
		//Nos traemos el historico de opraciones por robot
		//EM60 - EM240 - EM1440 - EM10080
		//T60 - T240 - T1440 - T10080
		
		List<Double> historicoParaChartDto = DemolabParaChartDto.getDtoParaChartDeEspMat(demolabDataService.findListaParaChartDemolabByRobot(robot.toUpperCase()));

		if(historicoParaChartDto.size() > 0) {
			historicoParaChartDto = historicoParaChartDto.subList(0, historicoParaChartDto.size());
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
			
			chart.addSeries("EM", periodos, historicoParaChartDto);
			//BitmapEncoder.saveBitmap(chart, nombreFicheroChart.toString(), BitmapFormat.PNG);
			try {
				BitmapEncoder.saveBitmap(chart, response.getOutputStream(), BitmapFormat.PNG);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

		
    
	
}
