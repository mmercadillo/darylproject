package daryl.system.robot.variance.predictor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.model.Robot;
import daryl.system.model.VarianceConfig;
import daryl.system.model.historicos.HistAudCad;
import daryl.system.robot.variance.calculation.StockPredict;
import daryl.system.robot.variance.predictor.base.VariancePredictor;
import daryl.system.robot.variance.repository.IHistAudCadRepository;
import daryl.system.robot.variance.repository.IVarianceConfigRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class VarianceAudcad  extends VariancePredictor{

	
	@Autowired
	IVarianceConfigRepository varianceConfigRepository;
	@Autowired
	private IHistAudCadRepository histAudCadRepository;


	private Double getPrediccionAnterior(List<Datos> datosForecast, VarianceConfig varianceConfig, Robot bot) {
		
		//Lista para prediccionAnterior
		List<Datos> datosForecastAnterior = datosForecast.subList(0, datosForecast.size()-1);
		
		int n = varianceConfig.getN();
		int offset = varianceConfig.getOffset();
		double alpha = varianceConfig.getAlpha();
		double beta = varianceConfig.getBeta();
		
		StockPredict stock = new StockPredict(datosForecast, bot.getActivo(), offset, n, alpha, beta);
		double[] priceVariance = stock.getPriceVariance();
		
		double prediccionAnterior = priceVariance[0];	
		
		logger.info("PREDICCIÓN ANTERIOR PARA EL ROBOT : {}", prediccionAnterior);
		return prediccionAnterior;

	}
	
	
	@Override
	protected Double calcularPrediccion(Robot bot) {
		
		Double prediccion = 0.0;
		
		try {
		
			List<HistAudCad> historico = histAudCadRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());
			VarianceConfig varianceConfig = varianceConfigRepository.findVarianceConfigByRobot(bot.getVarianceConfig());
			
			if(varianceConfig != null) {
				
				List<Datos> datosForecast = toDatosList(historico);
				Double prediccionAnterior = getPrediccionAnterior(datosForecast, varianceConfig, bot);
				
				int n = varianceConfig.getN();
				int offset = varianceConfig.getOffset();
				double alpha = varianceConfig.getAlpha();
				double beta = varianceConfig.getBeta();

	        	try {
	        		
	        		
	        		StockPredict stock = new StockPredict(datosForecast, bot.getActivo(), offset, n, alpha, beta);
	        		double[] priceVariance = stock.getPriceVariance();
	        		
	        		double forecast = priceVariance[0];
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
