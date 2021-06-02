package daryl.system.robot.arima.a3.predictor.base;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Orden;
import daryl.system.model.Prediccion;
import daryl.system.model.Robot;
import daryl.system.robot.arima.a3.repository.IOrdenRepository;
import daryl.system.robot.arima.a3.repository.IPrediccionRepository;

public abstract class ArimaPredictor {


	@Autowired
	protected Logger logger;
	
	@Autowired
	protected ConfigData config;
		
	@Autowired
	protected IOrdenRepository ordenRepository;
	@Autowired
	protected IPrediccionRepository prediccionRepository;

	protected abstract Double calcularPrediccion(Robot robot);



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
			
			//recuperamos la orden existente en TF 10080
			String estrategia = "ARIMA_" + robot.getActivo() + "_1440";
			Orden orden1440 = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), estrategia);
			
			if(orden1440 != null) {
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


	public void calculate(Robot bot) {
		
		logger.info("SE CALCULA LA PREDICCIÓN -> Robot -> " + bot);		
		Double prediccion = calcularPrediccion(bot);
		logger.info("PREDICCIÓN CALCULADA -> Robot -> " + bot + " Predicción -> " + prediccion);
		
		logger.info("SE CALCULA LA ORDEN -> Robot -> " + bot);		
		Orden orden = calcularOperacion(bot, prediccion, bot.getInverso());
		logger.info("ORDEN CALCULADA -> Robot -> " + bot + " -> Orden -> " + orden);
		
		Long fechaHoraMillis = System.currentTimeMillis();
		
		actualizarPrediccionBDs(bot, orden.getTipoOrden(), prediccion, fechaHoraMillis);
		logger.info("PREDICCIÓN ACTUALZIDA -> Robot -> " + bot + " Predicciñon -> " + prediccion);
		actualizarUltimaOrden(bot, orden, fechaHoraMillis);
		logger.info("ORDEN ANTERIOR ELIMINADA -> Robot -> " + bot);
		guardarNuevaOrden(orden, fechaHoraMillis);
		logger.info("NUEVA ORDEN GUARDADA -> Robot -> " + bot + " -> Orden -> " + orden);

		
	}

}
