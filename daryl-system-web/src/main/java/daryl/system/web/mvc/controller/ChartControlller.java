package daryl.system.web.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import daryl.system.model.HistoricoOperaciones;
import daryl.system.web.mvc.dto.HistoricoParaChartDto;
import daryl.system.web.services.IChartDataRobotService;

@RestController
public class ChartControlller {

	@Autowired
	IChartDataRobotService charDataRobotService;

 
	@GetMapping("/chart/{robot}/em")
    public void chartEspMat(@PathVariable String robot, HttpServletResponse response) {

		
		final List<HistoricoOperaciones> hist = charDataRobotService.findListaParaChartByRobot(robot);
		List<Double> historicoParaChartDto = HistoricoParaChartDto.getDtoParaChartDeEspMat(hist);

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
	
	
	
	@GetMapping("/chart/{robot}/total")
    public void chartTotal(@PathVariable String robot, HttpServletResponse response) {

		List<HistoricoOperaciones> hist = charDataRobotService.findListaParaChartByRobot(robot);
		List<Double> historicoParaChartDto = HistoricoParaChartDto.getDtoParaChartDeTotales(hist);

		
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
