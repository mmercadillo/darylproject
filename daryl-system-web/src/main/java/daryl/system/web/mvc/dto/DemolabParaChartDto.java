package daryl.system.web.mvc.dto;

import java.util.ArrayList;
import java.util.List;

import daryl.system.model.DemolabOps;
import daryl.system.model.HistoricoOperaciones;
import lombok.Getter;
import lombok.Setter;

public class DemolabParaChartDto{

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

	
	
	public static List<Long> getDtoParaChartDeTotales(List<DemolabOps> historico) {

		List<Long> datosParaChart = new ArrayList<Long>();
		Long acumulado = 0L;
		if(historico != null && historico.size() > 0) {
			for (DemolabOps hist : historico) {
				acumulado += Math.round(hist.getProfit());
				datosParaChart.add(acumulado);
			} 
		}
		return datosParaChart;
		
	}

	public static List<Double> getDtoParaChartDeEspMat(List<DemolabOps> historico) {

		List<Double> datosParaChart = new ArrayList<Double>();
		Long acumulado = 0L;
		Long opsWin = 0L;
		Long opsLoss = 0L;
		Long opsTotales = 0L;
		
		
		Double ganacias = 0.0;
		Double perdidas = 0.0;
		
		Double pMedia = 0.0;
		Double gMedia = 0.0;
		
		Double pWin = 0.0;
		Double pLoss = 0.0;
		
		if(historico != null && historico.size() > 0) {
			for (DemolabOps hist : historico) {
				
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
					
					double perdidaMediaPorOpLoss = (double)perdidas / (double)opsLoss;
					double gananciaMediaPorOpWin = (double)ganacias / (double)opsWin;

					double probWin = (double)opsWin / (double)opsTotales;
					double probLoss = (double)opsLoss / (double)opsTotales;

					em = (gananciaMediaPorOpWin * probWin) + (perdidaMediaPorOpLoss * probLoss);
					datosParaChart.add(em);
					
					pMedia = perdidaMediaPorOpLoss;
					gMedia = gananciaMediaPorOpWin;
					pWin = probWin;
					pLoss = probLoss;
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				

				
			} 
		}
		double em = (pWin * gMedia) + (pLoss * pMedia);
		return datosParaChart;
		
	}

	
}
