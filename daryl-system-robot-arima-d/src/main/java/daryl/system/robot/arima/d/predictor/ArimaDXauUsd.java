package daryl.system.robot.arima.d.predictor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.espy.arima.ArimaForecaster;
import org.espy.arima.DefaultArimaForecaster;
import org.espy.arima.DefaultArimaProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.MaxMinNormalizer;

import daryl.system.comun.dataset.enums.Mode;
import daryl.system.model.ArimaConfig;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.arima.d.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.d.repository.IArimaConfigRepository;
import daryl.system.robot.arima.d.repository.IHistoricoRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaDXauUsd  extends ArimaPredictor{

	@Autowired
	IArimaConfigRepository arimaConfigRepository;
	@Autowired
	private IHistoricoRepository historicoRepository; 
	

	private Double getPrediccionAnterior(List<Double> datosForecast, DefaultArimaProcess arimaProcess, ArimaConfig arimaConfig) {
		
		//Lista para prediccionAnterior
		List<Double> datosForecastAnterior = datosForecast.subList(0, datosForecast.size()-1);
		
    	List<Double> aux = datosForecastAnterior;
    	if(datosForecastAnterior.size() > arimaConfig.getInicio()) {
    		aux = datosForecastAnterior.subList((datosForecastAnterior.size()-arimaConfig.getInicio()), datosForecastAnterior.size());
    	}

		double[] observations = new double[aux.size()];
		for(int i = 0; i < aux.size(); i++) {
			observations[i] = aux.get(i).doubleValue();
		}
		
		ArimaForecaster arimaForecaster = null;
		arimaForecaster = new DefaultArimaForecaster(arimaProcess, observations);
		
		double prediccionAnterior = arimaForecaster.next();	
		
		logger.info("PREDICCIÓN ANTERIOR PARA EL ROBOT : {}", prediccionAnterior);
		return prediccionAnterior;

	}
	
	
	@Override
	protected Double calcularPrediccion(Robot bot) {
		
		
		Double prediccion = 0.0;
		try {
		
				
			ArimaConfig arimaConfig = arimaConfigRepository.findArimaConfigByRobot(bot.getArimaConfig());
			if(arimaConfig != null) {
				
				DefaultArimaProcess arimaProcess = (DefaultArimaProcess)getArimaProcess(arimaConfig);
				
				List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(bot.getTimeframe(), bot.getActivo());
				BarSeries serieParaCalculo = generateBarList(historico,  "BarSeries_" + bot.getTimeframe() + "_" + bot.getActivo(), bot.getActivo().getMultiplicador());
				MaxMinNormalizer darylNormalizer =  new MaxMinNormalizer(serieParaCalculo, Mode.CLOSE);
				List<Double> datos = darylNormalizer.getDatos();
				
				Double prediccionAnterior = getPrediccionAnterior(datos, arimaProcess, arimaConfig);
	
		    	List<Double> aux = datos;
		    	if(datos.size() > arimaConfig.getInicio()) {
		    		aux = datos.subList((datos.size()-arimaConfig.getInicio()), datos.size());
		    	}
		    	
		    	//List<Double> aux = data.subList((data.size()-inicio), data.size())
		    	double[] observations = new double[aux.size()];
		    	for(int i = 0; i < aux.size(); i++) {
		    		observations[i] = aux.get(i).doubleValue();
		    	}
	
		    	ArimaForecaster arimaForecaster = null;
		    	try {
	        		arimaForecaster = new DefaultArimaForecaster(arimaProcess, observations);
	        		
	        		double forecast = arimaForecaster.next();
	        		logger.info("Robot -> " + bot.getRobot() + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + prediccionAnterior);
	    	        if(forecast > prediccionAnterior) {
	    	        	prediccion = 1.0;
	    	        }
	    	        if(forecast < prediccionAnterior) {
	    	        	prediccion = -1.0;
	    	        }
	        		
	        	}catch (Exception e) {
	        		logger.error("No se ha podido calcular la prediccion para el robot: {}", bot.getRobot(), e);
	        	}
			}else {
				logger.info("No existe config para el robot: {}", bot.getRobot());
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
