package daryl.system.robot.variance.b.predictor.base;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Mode;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Orden;
import daryl.system.model.Prediccion;
import daryl.system.model.Robot;
import daryl.system.model.VarianceConfig;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.variance.b.repository.IHistoricoRepository;
import daryl.system.robot.variance.b.repository.IOrdenRepository;
import daryl.system.robot.variance.b.repository.IPrediccionRepository;
import daryl.system.robot.variance.b.repository.IVarianceConfigRepository;
import daryl.variance.StockPredict;

public abstract class VarianceBPredictor {


	@Autowired
	protected Logger logger;
	
	@Autowired
	protected ConfigData config;
		
	@Autowired
	protected IOrdenRepository ordenRepository;
	@Autowired
	protected IPrediccionRepository prediccionRepository;

	@Autowired
	protected IVarianceConfigRepository varianceConfigRepository;
	@Autowired
	protected IHistoricoRepository historicoRepository; 

	
	protected Double calcularPrediccion(Robot bot) {
		
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

	protected Double getPrediccionAnterior(List<Double> datosForecast, VarianceConfig varianceConfig) throws Exception {
		
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
	
	
	private void actualizarPrediccionBDs(Robot robot, TipoOrden orden, Double prediccionCierre, Long fechaHoraMillis) {
		try {
			
			//Creamos el bean prediccion
			Prediccion prediccion = new Prediccion();
				prediccion.setCierre(prediccionCierre);
				prediccion.setEstrategia(robot.getEstrategia());
				prediccion.setTipoActivo(robot.getActivo());
				prediccion.setTipoOrden(orden);
				prediccion.setFechaHora(fechaHoraMillis);
				prediccion.setFecha(config.getFechaInString(fechaHoraMillis));
				prediccion.setHora(config.getHoraInString(fechaHoraMillis));
				prediccion.setRobot(robot.getRobot());
				
			prediccionRepository.save(prediccion);
		} catch (Exception e) {
			logger.error("No se ha podido guardar la prediccion para el robot: {}", robot.getRobot(), e);
		}
	}
	

	private void actualizarUltimaOrden(Robot robot, Orden orden, Long fechaHoraMillis) {
		try {

			Orden ultimaOrden = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), robot.getEstrategia());
			if(ultimaOrden != null) {
				ultimaOrden.setFBaja(fechaHoraMillis);
				ordenRepository.delete(ultimaOrden);
				
			}else {
				logger.info("No hay orden para {} para actualzar del robot", robot.getRobot());
			}
		}catch (Exception e) {
			logger.error("No se ha recuperado el valor de la última orden del robot: {}", robot.getRobot(), e);
		}
	}
	
	private void guardarNuevaOrden(Orden orden, Long fechaHoraMillis) {
		try {
			orden.setFAlta(fechaHoraMillis);
			ordenRepository.save(orden);
		}catch (Exception e) {
			logger.error("No se ha podido guardar la nueva orden para el robot: {}", orden.getRobot(), e);
		}
	}
	
	private final Orden calcularOperacion(Robot robot, Double prediccion, Boolean inv) {
		
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
		if(prediccion < 0.0) {
			orden.setTipoOrden(TipoOrden.SELL);
			if(inv == Boolean.TRUE) orden.setTipoOrden(TipoOrden.BUY);
		}else if(prediccion > 0.0) {
			orden.setTipoOrden(TipoOrden.BUY);
			if(inv == Boolean.TRUE) orden.setTipoOrden(TipoOrden.SELL);
		}else {
			orden.setTipoOrden(TipoOrden.CLOSE);	
		}
		
		return orden;
	}


	public void calculate(Robot bot) {
		
		logger.info("SE CALCULA LA PREDICCIÓN -> Robot -> " + bot.getRobot());		
		Double prediccion = calcularPrediccion(bot);
		logger.info("PREDICCIÓN CALCULADA -> Robot -> " + bot.getRobot() + " Predicción -> " + prediccion);
		
		logger.info("SE CALCULA LA ORDEN -> Robot -> " + bot.getRobot());		
		Orden orden = calcularOperacion(bot, prediccion, bot.getInverso());
		logger.info("ORDEN CALCULADA -> Robot -> " + bot.getRobot() + " -> Orden -> " + orden.getTipoOrden());
		
		Long fechaHoraMillis = System.currentTimeMillis();
		
		actualizarPrediccionBDs(bot, orden.getTipoOrden(), prediccion, fechaHoraMillis);
		logger.info("PREDICCIÓN ACTUALZIDA -> Robot -> " + bot.getRobot() + " Predicciñon -> " + prediccion);
		actualizarUltimaOrden(bot, orden, fechaHoraMillis);
		logger.info("ORDEN ANTERIOR ELIMINADA -> Robot -> " + bot.getRobot());
		guardarNuevaOrden(orden, fechaHoraMillis);
		logger.info("NUEVA ORDEN GUARDADA -> Robot -> " + bot.getRobot() + " -> Orden -> " + orden.getTipoOrden());

		
	}


	
}
