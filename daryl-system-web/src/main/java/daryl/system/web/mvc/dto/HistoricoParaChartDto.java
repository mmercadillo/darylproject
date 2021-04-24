package daryl.system.web.mvc.dto;

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

	
	
	public static List<Long> getDtoParaChartDeTotales(List<HistoricoOperaciones> historico) {

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

	public static List<Double> getDtoParaChartDeEspMat(List<HistoricoOperaciones> historico) {

		List<Double> datosParaChart = new ArrayList<Double>();
		Long acumulado = 0L;
		Long opsWin = 0L;
		Long opsLoss = 0L;
		Long opsTotales = 0L;
		
		
		Double ganacias = 0.0;
		Double perdidas = 0.0;
		
		if(historico != null && historico.size() > 0) {
			for (HistoricoOperaciones hist : historico) {
				
				opsTotales += 1;
				if(hist.getProfit() > 0) {
					opsWin += 1;
					ganacias += hist.getProfit();
				}else {
					opsLoss += 1;
					perdidas += hist.getProfit();
				}
				
				double em = 0.0;
				try {
					
					double perdidaMediaPorOpLoss = perdidas / opsLoss;
					double gananciaMediaPorOpWin = ganacias / opsWin;

					double probWin = (double)opsWin / opsTotales;
					double probLoss = (double)opsLoss / opsTotales;

					em = (gananciaMediaPorOpWin * probWin) + (perdidaMediaPorOpLoss * probLoss * (-1));
					datosParaChart.add(em);
				}catch (Exception e) {
					// TODO: handle exception
				}
				

				
			} 
		}
		return datosParaChart;
		
	}

	
}
