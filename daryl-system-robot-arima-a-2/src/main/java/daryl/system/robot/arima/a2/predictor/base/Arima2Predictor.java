package daryl.system.robot.arima.a2.predictor.base;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.arima.gen.ARIMA;
import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Mode;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Orden;
import daryl.system.model.Prediccion;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.arima.a2.repository.IHistoricoRepository;
import daryl.system.robot.arima.a2.repository.IOrdenRepository;
import daryl.system.robot.arima.a2.repository.IPrediccionRepository;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class Arima2Predictor {


	@Autowired
	protected Logger logger;
	
	@Autowired
	protected ConfigData config;
		
	@Autowired
	protected IOrdenRepository ordenRepository;
	@Autowired
	protected IPrediccionRepository prediccionRepository;


	@Autowired
	private IHistoricoRepository historicoRepository; 

	protected Double calcularPrediccion(Robot bot) {

		Double prediccion = 0.0;
		
		List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(bot.getTimeframe(), bot.getActivo());
		BarSeries serieParaCalculo = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + bot.getTimeframe() + "_" + bot.getActivo(), bot.getActivo().getMultiplicador());
		MaxMinNormalizer darylNormalizer =  new MaxMinNormalizer(serieParaCalculo, Mode.CLOSE);
		List<Double> datos = darylNormalizer.getDatos();
		
		try {
			
			ARIMA arima=new ARIMA(datos.stream().mapToDouble(Double::new).toArray());
			
			int []model=arima.getARIMAmodel();

			double forecast = (double)arima.aftDeal(arima.predictValue(model[0],model[1]));
			logger.info("Robot -> " + bot.getRobot() + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + datos.get(datos.size()-1));
			if(forecast > datos.get(datos.size()-1) ) {
				prediccion = 1.0;
			}else if(forecast < datos.get(datos.size()-1)) {
				prediccion = -1.0;
			}else {
				prediccion = 0.0;
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
	
	private void actualizarUltimaOrden(Robot robot, Orden orden, Long fechaHoraMillis) {
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
			
			//recuperamos la orden existente en TF 10080
			String estrategia = "ARIMA_" + robot.getActivo() + "_10080";
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

	@Transactional
	public void calculate(Robot bot) throws IOException{
		
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
