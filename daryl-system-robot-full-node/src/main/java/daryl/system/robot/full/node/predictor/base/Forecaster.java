package daryl.system.robot.full.node.predictor.base;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Orden;
import daryl.system.model.Prediccion;
import daryl.system.model.Robot;
import daryl.system.robot.full.node.repository.IHistoricoRepository;
import daryl.system.robot.full.node.repository.IOrdenRepository;
import daryl.system.robot.full.node.repository.IPrediccionRepository;

public abstract class Forecaster {

	@Autowired
	protected Logger logger;
	@Autowired
	protected ConfigData config;
	@Autowired
	protected IOrdenRepository ordenRepository;
	@Autowired
	protected IPrediccionRepository prediccionRepository;
	@Autowired
	protected IHistoricoRepository historicoRepository; 

	public abstract Double calcularPrediccion(Robot bot);
	
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
	public synchronized void actualizarPrediccionBDs(Robot robot, TipoOrden orden, Double prediccionCierre, Long fechaHoraMillis) {
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
	
	@Transactional
	public synchronized void actualizarUltimaOrden(Robot robot, Long fechaHoraMillis) {
		try {

			List<Orden> ordenes = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), robot.getEstrategia());
			
			if(ordenes != null) {
				logger.info("ORDENES A BORRAR DEL robot " + robot.getRobot() + " - " + ordenes.size());
				for(Orden orden : ordenes) {
					ordenRepository.delete(orden);
					logger.info("ORDEN BORRADA DEL robot " + robot.getRobot());
				}
				
			}else {
				logger.info("No hay orden para actualizar del robot " + robot.getRobot());
			}
		}catch (Exception e) {
			logger.error("No se ha recuperado el valor de la última orden del robot: {}", robot.getRobot(), e);
		}
	}
	
	public synchronized void guardarNuevaOrden(Orden orden, Long fechaHoraMillis) {
		try {
			orden.setFAlta(fechaHoraMillis);
			ordenRepository.save(orden);
		}catch (Exception e) {
			logger.error("No se ha podido guardar la nueva orden para el robot: {}", orden.getRobot(), e);
		}
	}

	//@Async
	public void calculate(Robot bot) {
		
		if(bot.getRobotActivo() == Boolean.TRUE) {
			
			logger.info("SE CALCULA LA PREDICCIÓN -> Robot -> " + bot.getRobot());		
			Double prediccion = calcularPrediccion(bot);
			logger.info("PREDICCIÓN CALCULADA -> Robot -> " + bot.getRobot() + " Predicción -> " + prediccion);
			
			logger.info("SE CALCULA LA ORDEN -> Robot -> " + bot.getRobot());		
			Orden orden = calcularOperacion(bot, prediccion, bot.getInverso());
			logger.info("ORDEN CALCULADA -> Robot -> " + bot.getRobot() + " -> Orden -> " + orden.getTipoOrden());
			
			Long fechaHoraMillis = System.currentTimeMillis();
			
			actualizarPrediccionBDs(bot, orden.getTipoOrden(), prediccion, fechaHoraMillis);
			logger.info("PREDICCIÓN ACTUALZIDA -> Robot -> " + bot.getRobot() + " Prediccion -> " + prediccion);
			actualizarUltimaOrden(bot, fechaHoraMillis);
			logger.info("ORDEN ANTERIOR ELIMINADA -> Robot -> " + bot.getRobot());
			guardarNuevaOrden(orden, fechaHoraMillis);
			logger.info("NUEVA ORDEN GUARDADA -> Robot -> " + bot.getRobot() + " -> Orden -> " + orden.getTipoOrden());			
			
		}else {
			
			logger.info("NO SE CALCULA LA ORDEN PARA EL ROBOT -> " + bot.getRobot() + " -> NO ESTÁ ACTIVO ");			
			
		}


	}


}
