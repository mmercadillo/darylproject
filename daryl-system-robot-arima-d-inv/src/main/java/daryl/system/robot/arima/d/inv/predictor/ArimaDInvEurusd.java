package daryl.system.robot.arima.d.inv.predictor;

import java.util.ArrayList;
import java.util.List;

import org.espy.arima.ArimaForecaster;
import org.espy.arima.DefaultArimaForecaster;
import org.espy.arima.DefaultArimaProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.model.ArimaConfig;
import daryl.system.model.Robot;
import daryl.system.model.historicos.HistEurUsd;
import daryl.system.robot.arima.d.inv.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.d.inv.repository.IArimaConfigRepository;
import daryl.system.robot.arima.d.inv.repository.IHistEurUsdRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaDInvEurusd  extends ArimaPredictor{
	

	
	@Autowired
	IArimaConfigRepository arimaConfigRepository;
	@Autowired
	private IHistEurUsdRepository histEurUsdRepository;


	private Double getPrediccionAnterior(List<Datos> datosForecast, DefaultArimaProcess arimaProcess, ArimaConfig arimaConfig) {
		
		//Lista para prediccionAnterior
		List<Datos> datosForecastAnterior = datosForecast.subList(0, datosForecast.size()-1);
		//Recuperamos los cierres de cada Dato
		DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		List<Double> datosAnterior = darylNormalizer.getDatos();

		
    	List<Double> aux = datosAnterior;
    	if(datosAnterior.size() > arimaConfig.getInicio()) {
    		aux = datosAnterior.subList((datosAnterior.size()-arimaConfig.getInicio()), datosAnterior.size());
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
		
			List<HistEurUsd> historico = histEurUsdRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());

			ArimaConfig arimaConfig = arimaConfigRepository.findArimaConfigByRobot(bot.getArimaConfig());
			if(arimaConfig != null) {
				DefaultArimaProcess arimaProcess = (DefaultArimaProcess)getArimaProcess(arimaConfig);
			
				List<Datos> datosForecast = toDatosList(historico);
				Double prediccionAnterior = getPrediccionAnterior(datosForecast, arimaProcess, arimaConfig);
				
				//Recuperamos los cierres de cada Dato
				DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
				List<Double> datos = darylNormalizer.getDatos();
				
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

	
	private List<Datos> toDatosList(List<HistEurUsd> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (HistEurUsd hist : historico) {
			
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
	

	
	
}
