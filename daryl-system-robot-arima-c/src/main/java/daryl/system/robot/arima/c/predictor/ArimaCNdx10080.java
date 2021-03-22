package daryl.system.robot.arima.c.predictor;

import java.util.ArrayList;
import java.util.List;

import org.espy.arima.ArimaForecaster;
import org.espy.arima.DefaultArimaForecaster;
import org.espy.arima.DefaultArimaProcess;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.model.ArimaConfig;
import daryl.system.model.Orden;
import daryl.system.model.Robot;
import daryl.system.model.historicos.HistNdx;
import daryl.system.robot.arima.c.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.c.repository.IArimaConfigRepository;
import daryl.system.robot.arima.c.repository.IHistNdxRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaCNdx10080  extends ArimaPredictor{
	

	@Autowired
	IArimaConfigRepository arimaConfigRepository;


	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistNdxRepository histNdxRepository;
	

	private Integer inicio;
	


	@Override
	protected Double calcularPrediccion(Robot bot) {

		Double prediccion = 0.0;

		List<HistNdx> historico = histNdxRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());
		
		
		List<Datos> datosForecast = toDatosList(historico);
		//List<Datos> datosT = loader.loadDatos(configuracion.getFHistoricoLearn());
		
		List<Datos> datosTotal = new ArrayList<Datos>();
		datosTotal.addAll(datosForecast);
		darylNormalizer.setDatos(datosTotal, Mode.CLOSE);
		
		List<Double> datos = darylNormalizer.getDatos();
		
		
		try {



			ArimaConfig arimaConfig = arimaConfigRepository.findArimaConfigByRobot(bot.getRobot());
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
		        if(forecast > ultimoDato) {
		        	prediccion = 1.0;
		        }
		        if(forecast < ultimoDato) {
		        	prediccion = -1.0;
		        }
        	}catch (Exception e) {
        	}

		}catch (Exception e) {
			
		}

		return prediccion;
	
	
	}

	
	private List<Datos> toDatosList(List<HistNdx> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (HistNdx hist : historico) {
			
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
