package daryl.system.robot.variance.predictor.base;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
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
import daryl.system.robot.variance.repository.IHistoricoRepository;
import daryl.system.robot.variance.repository.IOrdenRepository;
import daryl.system.robot.variance.repository.IPrediccionRepository;
import daryl.system.robot.variance.repository.IVarianceConfigRepository;
import daryl.variance.StockPredict;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class VariancePredictor {


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
	
	private void actualizarUltimaOrden(Robot robot, Orden nuevaOrden, Long fechaHoraMillis) {
		
		try {
			long millis = System.currentTimeMillis();
			
			Orden ordenGuardada = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), robot.getEstrategia());
			
			if(ordenGuardada != null && ordenGuardada.getTipoOrden() != nuevaOrden.getTipoOrden()) {
				
				logger.info("ACTUALIZAMOS LA ORDEN PARA EL ROBOT " + robot.getRobot());
				ordenGuardada.setFecha(config.getFechaInString(millis));
				ordenGuardada.setHora(config.getHoraInString(millis));
				ordenGuardada.setTipoOrden(nuevaOrden.getTipoOrden());
				ordenGuardada.setFAlta(nuevaOrden.getFAlta());
				ordenRepository.save(ordenGuardada);
				logger.info("ORDEN PARA EL ROBOT " + robot.getRobot() + " ACTUALIZADA -> " + ordenGuardada.getTipoOrden());
				
			}else if(ordenGuardada == null){
				
				logger.info("NO EXISTE ORDEN PARA EL ROBOT " + robot.getRobot() + " DAMOS DE ALTA UNA NUEVA -> " + nuevaOrden.getTipoOrden());
				
				ordenRepository.save(nuevaOrden);
				
				logger.info("ORDEN PARA EL ROBOT " + robot.getRobot() + " DADA DE ALTA -> " + nuevaOrden.getTipoOrden());
			}else {
				logger.info("NO ES NECESARIO ACTUALIZAR LA ORDEN PARA EL ROBOT " + robot.getRobot());
			}
			
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		/*
		try {

			//Orden ultimaOrden = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), robot.getEstrategia());
			List<Orden> ultimasOrdenes = ordenRepository.findAllBytipoActivoAndEstrategia(robot.getActivo(), robot.getEstrategia());
			if(ultimasOrdenes != null && ultimasOrdenes.size() > 0) {
				//ultimaOrden.setFBaja(fechaHoraMillis);
				for (Orden ord : ultimasOrdenes) {
					ordenRepository.delete(ord);
				}
				
				
			}else {
				logger.info("No hay orden para {} para actualzar del robot", robot.getRobot());
			}
		}catch (Exception e) {
			logger.error("No se ha recuperado el valor de la última orden del robot: {}", robot.getRobot(), e);
		}
		*/
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
	
	@Transactional
	public void calculate(Robot bot) throws IOException {
		
		if(bot.getRobotActivo() == Boolean.TRUE) {
			
			logger.info("SE CALCULA LA PREDICCIÓN -> Robot -> " + bot.getRobot());		
			Double prediccion = calcularPrediccion(bot);
			logger.info("PREDICCIÓN CALCULADA -> Robot -> " + bot.getRobot() + " Predicción -> " + prediccion);
			
			logger.info("SE CALCULA LA ORDEN -> Robot -> " + bot.getRobot());		
			Orden orden = calcularOperacion(bot, prediccion, bot.getInverso());
			logger.info("ORDEN CALCULADA -> Robot -> " + bot.getRobot() + " -> Orden -> " + orden.getTipoOrden());
			
			Long fechaHoraMillis = System.currentTimeMillis();
			
			//actualizarPrediccionBDs(bot, orden.getTipoOrden(), prediccion, fechaHoraMillis);
			logger.info("PREDICCIÓN ACTUALZIDA -> Robot -> " + bot.getRobot() + " Predicciñon -> " + prediccion);
			actualizarUltimaOrden(bot, orden, fechaHoraMillis);
			//logger.info("ORDEN ANTERIOR ELIMINADA -> Robot -> " + bot.getRobot());
			//guardarNuevaOrden(orden, fechaHoraMillis);
			//logger.info("NUEVA ORDEN GUARDADA -> Robot -> " + bot.getRobot() + " -> Orden -> " + orden.getTipoOrden());
			
		}
	}


	
}
