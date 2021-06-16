package daryl.system.robot.full.node.predictor.varianceB;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.system.comun.enums.Mode;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Orden;
import daryl.system.model.Robot;
import daryl.system.model.VarianceConfig;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.full.node.predictor.base.Forecaster;
import daryl.system.robot.full.node.repository.IVarianceConfigRepository;
import daryl.variance.StockPredict;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class VarianceB2Forecaster  extends Forecaster{

	@Autowired
	private IVarianceConfigRepository varianceConfigRepository;

	@Override
	public Double calcularPrediccion(Robot bot) {
		
		Double prediccion = 0.0;
		
		try {
			
			
			
			VarianceConfig varianceConfig = varianceConfigRepository.findVarianceConfigByRobot(bot.getVarianceConfig());
			if(varianceConfig != null) {
			
				List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(bot.getTimeframe(), bot.getActivo());
				BarSeries serieParaCalculo = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + bot.getTimeframe() + "_" + bot.getActivo(), bot.getActivo().getMultiplicador());
				MaxMinNormalizer darylNormalizer =  new MaxMinNormalizer(serieParaCalculo, Mode.CLOSE);
				List<Double> datos = darylNormalizer.getDatos();
				
				Double prediccionAnterior = getPrediccionAnterior(datos, varianceConfig);
				
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
	

	public Double getPrediccionAnterior(List<Double> datosForecast, VarianceConfig varianceConfig) throws Exception {
		
		//Lista para prediccionAnterior
		List<Double> datosForecastAnterior = datosForecast.subList(0, datosForecast.size()-1);

		
		int n = varianceConfig.getN();
		int offset = varianceConfig.getOffset();
		double alpha = varianceConfig.getAlpha();
		double beta = varianceConfig.getBeta();
		int m = varianceConfig.getLastM();
		
		StockPredict stock = new StockPredict(datosForecastAnterior, offset, n, alpha, beta, m);
		double[] priceVariance = stock.getPriceVariance();
		
		double prediccionAnterior = priceVariance[0];

		logger.info("PREDICCIÓN ANTERIOR PARA EL ROBOT : {}", prediccionAnterior);
		return prediccionAnterior;

	}
	
	@Override
	public Orden calcularOperacion(Robot robot, Double prediccion, Boolean inv) {
		
		long millis = System.currentTimeMillis();
		Orden orden = new Orden();
			orden.setFAlta(millis);
			orden.setFBaja(null);
			orden.setEstrategia(robot.getEstrategia());
			orden.setTipoActivo(robot.getActivo());
			orden.setTipoOrden(TipoOrden.CLOSE);
			orden.setRobot(robot.getRobot());
			orden.setFecha(config.getFechaInString(millis));
			orden.setHora(config.getHoraInString(millis));
			
			//recuperamos la orden existente en TF 10080
			String estrategia = "VARIANCE_B_" + robot.getActivo() + "_10080";
			Orden orden10080 = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), estrategia);
				
			if(orden10080 != null) {
				if(orden10080.getTipoOrden() == TipoOrden.SELL) {
					if(prediccion <= 0.0 && inv == Boolean.FALSE) {
						orden.setTipoOrden(TipoOrden.SELL);
					}else {
						
					}
					
					if(prediccion >= 0.0 && inv == Boolean.TRUE) {
						orden.setTipoOrden(TipoOrden.SELL);
					}else {
						//orden.setTipoOrden(TipoOrden.CLOSE);
					}
				}else if(orden10080.getTipoOrden() == TipoOrden.BUY) {
					if(prediccion >= 0.0 && inv == Boolean.FALSE) {
						orden.setTipoOrden(TipoOrden.BUY);
					}else {
						//orden.setTipoOrden(TipoOrden.CLOSE);
					}
					
					if(prediccion <= 0.0 && inv == Boolean.TRUE) {
						orden.setTipoOrden(TipoOrden.BUY);
					}else {
						//orden.setTipoOrden(TipoOrden.CLOSE);
					}
				}
			}
		
		return orden;
	}

	
}
