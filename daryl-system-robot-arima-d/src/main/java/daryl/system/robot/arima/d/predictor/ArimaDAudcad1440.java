package daryl.system.robot.arima.d.predictor;

import java.util.ArrayList;
import java.util.List;

import org.espy.arima.ArimaForecaster;
import org.espy.arima.DefaultArimaForecaster;
import org.espy.arima.DefaultArimaProcess;
import org.springframework.beans.factory.annotation.Autowired;

import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.model.ArimaConfig;
import daryl.system.model.Robot;
import daryl.system.model.historicos.HistAudCad;
import daryl.system.robot.arima.d.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.d.repository.IArimaConfigRepository;
import daryl.system.robot.arima.d.repository.IHistAudCadRepository;
import lombok.ToString;

//@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaDAudcad1440  extends ArimaPredictor{
	

	
	@Autowired
	IArimaConfigRepository arimaConfigRepository;


	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistAudCadRepository histAudCadRepository;


	private Integer inicio;



	static Double prediccionArimaAnterior = 0.0;
	@Override
	protected Double calcularPrediccion(Robot bot) {
		
		Double prediccion = 0.0;
		
		List<HistAudCad> historico = histAudCadRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());
		
		List<Datos> datosForecast = toDatosList(historico);
		//List<Datos> datosT = loader.loadDatos(configuracion.getFHistoricoLearn());
		List<Datos> datosTotal = new ArrayList<Datos>();
		datosTotal.addAll(datosForecast);
		darylNormalizer.setDatos(datosTotal, Mode.CLOSE);
		
		List<Double> datos = darylNormalizer.getDatos();
		
		datos.stream().forEach(dato -> {
			int pos = datos.indexOf(dato);
			datos.set(pos, dato * 10000);
		});
		
		try {


			ArimaConfig arimaConfig = arimaConfigRepository.findArimaConfigByRobot(bot.getArimaConfig());
			if(arimaConfig != null) {
				this.inicio = arimaConfig.getInicio();
				DefaultArimaProcess arimaProcess = (DefaultArimaProcess)getArimaProcess(arimaConfig);
	
		        
		    	List<Double> aux = datos;
		    	if(datos.size() > this.inicio) {
		    		aux = datos.subList((datos.size()-this.inicio), datos.size());
		    	}else {
		    		
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
	    	        double ultimoDato = datos.get(datos.size()-1);
	    	        
	    	        if(prediccionArimaAnterior != 0.0) {
	    	        	ultimoDato = prediccionArimaAnterior;
	    	        }
	    	        if(forecast > ultimoDato) {
	    	        	prediccion = 1.0;
	    	        }
	    	        if(forecast < ultimoDato) {
	    	        	prediccion = -1.0;
	    	        }
	    	        prediccionArimaAnterior = forecast;
	        		
	        	}catch (Exception e) {
	        		logger.error("No se ha podido calcular la prediccion para el robot: {}", bot.getRobot(), e);
	        	}
			}else {
				System.out.println("No existe config para " + bot.getRobot());
				logger.info("No existe config para el robot: {}", bot.getRobot());
			}
		}catch (Exception e) {
			logger.error("No se ha podido calcular la prediccion para el robot: {}", bot.getRobot(), e);
		}

		return prediccion;
	
	
	}

	
	private List<Datos> toDatosList(List<HistAudCad> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (HistAudCad hist : historico) {
			
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
