package daryl.system.robot.full.node.predictor.variance;

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
public class Variance3Forecaster  extends Forecaster{

	
	@Autowired
	private IVarianceConfigRepository varianceConfigRepository;

	@Override
	public Double calcularPrediccion(Robot bot) {
		
		Double prediccion = 0.0;
		
		
		List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(bot.getTimeframe(), bot.getActivo());
		BarSeries serieParaCalculo = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + bot.getTimeframe() + "_" + bot.getActivo(), bot.getActivo().getMultiplicador());
		MaxMinNormalizer darylNormalizer =  new MaxMinNormalizer(serieParaCalculo, Mode.CLOSE);
		List<Double> datos = darylNormalizer.getDatos();


		
		try {
			
			VarianceConfig varianceConfig = varianceConfigRepository.findVarianceConfigByRobot(bot.getVarianceConfig());
			if(varianceConfig != null) {
				
				int n = varianceConfig.getN();
				int offset = varianceConfig.getOffset();
				double alpha = varianceConfig.getAlpha();
				double beta = varianceConfig.getBeta();
				int m = varianceConfig.getLastM();
				
				try {
	        		
	        		StockPredict stock = new StockPredict(datos, offset, n, alpha, beta, m);
	        		double[] priceVariance = stock.getPriceVariance();
	        		
	        		double forecast = priceVariance[0];
	        		logger.info("Robot -> " + bot.getRobot() + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + datos.get(datos.size()-1));
	        		if(forecast > datos.get(datos.size()-1)) {
			        	prediccion = 1.0;
			        }
			        if(forecast < datos.get(datos.size()-1)) {
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
		String estrategia = "VARIANCE_" + robot.getActivo() + "_1440";
		List<Orden> ordenes = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), estrategia);
			
		if(ordenes != null && ordenes.size() > 0) {
			Orden orden1440 = ordenes.get(0);
			if(orden1440.getTipoOrden() == TipoOrden.SELL) {
				if(prediccion <= 0.0 && inv == Boolean.FALSE) {
					orden.setTipoOrden(TipoOrden.SELL);
				}else {
					
				}
				
				if(prediccion >= 0.0 && inv == Boolean.TRUE) {
					orden.setTipoOrden(TipoOrden.SELL);
				}else {
					//orden.setTipoOrden(TipoOrden.CLOSE);
				}
			}else if(orden1440.getTipoOrden() == TipoOrden.BUY) {
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
