package daryl.system.robots.client.web.mvc.dto;

import java.util.ArrayList;
import java.util.List;

import daryl.system.model.HistoricoOperaciones;
import lombok.Getter;
import lombok.Setter;

public class HistoricoParaChartDto{

	@Getter @Setter
	private Long id;
	@Getter @Setter
	private Long ticket;
	@Getter @Setter
	private Double cierre;
	@Getter @Setter
	private Double profit;
	@Getter @Setter
	private Double apertura;
	@Getter @Setter
	private Double swap;
	@Getter @Setter
	private Double comision;
	@Getter @Setter
	private String fcierre;
	@Getter @Setter
	private String fapertura;
	@Getter @Setter
	private String comentario;

	
	
	public static List<Long> getDtoParaChart(List<HistoricoOperaciones> historico) {

		List<Long> datosParaChart = new ArrayList<Long>();
		Long acumulado = 0L;
		if(historico != null && historico.size() > 0) {
			for (HistoricoOperaciones hist : historico) {
				acumulado += Math.round(hist.getProfit());
				datosParaChart.add(acumulado);
			} 
		}
		return datosParaChart;
		
	}


	
}
