package daryl.system.robot.variance.b.predictor;

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
import daryl.system.robot.variance.b.predictor.base.VariancePredictor;
import daryl.system.robot.variance.b.repository.IHistAudCadRepository;
import daryl.system.robot.variance.b.repository.IVarianceConfigRepository;
import daryl.variance.StockPredict;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class VarianceBEurusd  extends VariancePredictor{
	
	
	@Autowired
	IVarianceConfigRepository varianceConfigRepository;
	@Autowired
	private IHistAudCadRepository histEurUsdRepository;

	private Double getPrediccionAnterior(List<Datos> datosForecast, VarianceConfig varianceConfig) throws Exception {
		
		//Lista para prediccionAnterior
		List<Datos> datosForecastAnterior = datosForecast.subList(0, datosForecast.size()-1);
		//Recuperamos los cierres de cada Dato
		DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(datosForecastAnterior, Mode.CLOSE);
		List<Double> datosAnterior = darylNormalizer.getDatos();
		
		int n = varianceConfig.getN();
		int offset = varianceConfig.getOffset();
		double alpha = varianceConfig.getAlpha();
		double beta = varianceConfig.getBeta();
		int m = varianceConfig.getLastM();
		
		StockPredict stock = new StockPredict(datosAnterior, offset, n, alpha, beta, m);
		double[] priceVariance = stock.getPriceVariance();
		
		double prediccionAnterior = priceVariance[0];

		logger.info("PREDICCIÓN ANTERIOR PARA EL ROBOT : {}", prediccionAnterior);
		return prediccionAnterior;

	}
	

	@Override
	protected Double calcularPrediccion(Robot bot) {
		
		Double prediccion = 0.0;
		try {
			
			
			List<HistAudCad> historico = histEurUsdRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());
			
			VarianceConfig varianceConfig = varianceConfigRepository.findVarianceConfigByRobot(bot.getVarianceConfig());
			if(varianceConfig != null) {
				
				List<Datos> datosForecast = toDatosList(historico);
				Double prediccionAnterior = getPrediccionAnterior(datosForecast, varianceConfig);
				
				//Recuperamos los cierres de cada Dato
				DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
				List<Double> datos = darylNormalizer.getDatos();

				
				int n = varianceConfig.getN();
				int offset = varianceConfig.getOffset();
				double alpha = varianceConfig.getAlpha();
				double beta = varianceConfig.getBeta();
				int m = varianceConfig.getLastM();
				
				try {
	        		
	        		StockPredict stock = new StockPredict(datos, offset, n, alpha, beta, m);
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
