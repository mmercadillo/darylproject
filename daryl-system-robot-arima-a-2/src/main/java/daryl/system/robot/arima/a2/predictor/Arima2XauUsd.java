package daryl.system.robot.arima.a2.predictor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

//logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.MaxMinNormalizer;

import daryl.arima.gen.ARIMA;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.arima.a2.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.a2.repository.IHistoricoRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class Arima2XauUsd  extends ArimaPredictor{


	//@Autowired
	//private IHistXauUsdRepository histXauUsdRepository;
	@Autowired
	private IHistoricoRepository historicoRepository; 
	
	@Override
	protected Double calcularPrediccion(Robot bot) {
		
		Double prediccion = 0.0;
		
		List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(bot.getTimeframe(), bot.getActivo());
		BarSeries serieParaCalculo = generateBarList(historico,  "BarSeries_" + bot.getTimeframe() + "_" + bot.getActivo(), bot.getActivo().getMultiplicador());
		MaxMinNormalizer darylNormalizer =  new MaxMinNormalizer(serieParaCalculo, Mode.CLOSE);
		List<Double> datos = darylNormalizer.getDatos();
		
		try {
			
			ARIMA arima=new ARIMA(datos.stream().mapToDouble(Double::new).toArray());
			
			int []model=arima.getARIMAmodel();

			double forecast = (double)arima.aftDeal(arima.predictValue(model[0],model[1]));
			logger.info("Robot -> " + bot.getRobot() + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + datos.get(datos.size()-1));
			if(forecast > datos.get(datos.size()-1)) {
				prediccion = 1.0;
			}else if(forecast < datos.get(datos.size()-1)) {
				prediccion = -1.0;
			}else {
				prediccion = 0.0;
			}
			
		}catch (Exception e) {
			logger.error("No se ha podido calcular la prediccion para el robot: {}", bot.getRobot(), e);
		}

		
		return prediccion;
	
	}

	/*
	private List<Datos> toDatosList(List<HistXauUsd> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (HistXauUsd hist : historico) {
			
			Datos dato = Datos.builder().fecha(hist.getFecha())
										.hora(hist.getHora())
										.apertura(hist.getApertura())
										.maximo(hist.getMaximo())
										.minimo(hist.getMinimo())
										.cierre(hist.getCierre())
										.volumen(hist.getVolumen())
										.build();
			datos.add(dato);
			
		}
		
		return datos;
		
		
	}
	*/
	
	
	private BarSeries  generateBarList(List<Historico> historico, String name, int multiplicador){
		
		BarSeries series = new BaseBarSeriesBuilder().withName(name).build();
		for (Historico hist : historico) {
			
			Long millis = hist.getFechaHora();
			
			Instant instant = Instant.ofEpochMilli(millis);
			ZonedDateTime barDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
			
			series.addBar(	barDateTime, 
							hist.getApertura() * multiplicador, 
							hist.getMaximo() * multiplicador, 
							hist.getMinimo() * multiplicador, 
							hist.getCierre() * multiplicador, 
							hist.getVolumen() * multiplicador);
			
		}
		
		return series;
		
		
	}

	
	
}
