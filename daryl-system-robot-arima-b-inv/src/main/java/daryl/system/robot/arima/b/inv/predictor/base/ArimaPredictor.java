package daryl.system.robot.arima.b.inv.predictor.base;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Orden;
import daryl.system.model.Prediccion;
import daryl.system.model.Robot;
import daryl.system.robot.arima.b.inv.repository.IOrdenRepository;
import daryl.system.robot.arima.b.inv.repository.IPrediccionRepository;

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

	private void actualizarPrediccionBDs(Activo activo, String estrategia, String robot, TipoOrden orden, Double prediccionCierre, Long fechaHoraMillis) {
		try {
			
			//Creamos el bean prediccion
			Prediccion prediccion = new Prediccion();
				prediccion.setCierre(prediccionCierre);
				prediccion.setEstrategia(estrategia);
				prediccion.setTipoActivo(activo);
				prediccion.setTipoOrden(orden);
				prediccion.setFechaHora(fechaHoraMillis);
				prediccion.setFecha(config.getFechaInString(fechaHoraMillis));
				prediccion.setHora(config.getHoraInString(fechaHoraMillis));
				prediccion.setRobot(robot);
				
			prediccionRepository.save(prediccion);
			//////logger.info("Guardamos la prediccion para {} es {}", activo.name(), prediccion);
		} catch (Exception e) {
			//logger.error("No se ha podido guardar la prediccion para el activo: {}", activo.name(), e);
		}
	}
	
	private void actualizarUltimaOrden(Activo activo, String estrategia, Orden orden, Long fechaHoraMillis) {
		try {
			//Recuperamos la orden sin fecha de fin (fBaja)
			////logger.info("Buscamos  la orden anterior para {} para actualizar",activo.name());
			Orden ultimaOrden = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, activo, estrategia);
			if(ultimaOrden != null) {
				////logger.info("Actualizamos la orden anterior para {} es {}", activo.name(), orden.getTipoOrden());
				ultimaOrden.setFBaja(fechaHoraMillis);
				//ordenRepository.saveAndFlush(ultimaOrden);
				ordenRepository.delete(ultimaOrden);
				////logger.info("Actualizamos la orden anterior para {} es {}", activo.name(), orden.getTipoOrden());
				
			}else {
				////logger.info("No hay orden para {} para actualziar", activo.name());
			}
		}catch (Exception e) {
			//logger.error("No se ha recuperado el valor de la última orden del activo: {}", activo.name(), e);
		}
	}
	
	
	private void guardarNuevaOrden(Orden orden, Long fechaHoraMillis) {
		try {
			orden.setFAlta(fechaHoraMillis);
			ordenRepository.save(orden);
			////logger.info("Guardamos la orden para {} es {}", orden.getTipoActivo().name(), orden.getTipoOrden());
		}catch (Exception e) {
			//logger.error("No se ha podido guardar la nueva orden para el activo: {}", orden.getTipoActivo().name(), e);
		}
	}
	
	private final Orden calcularOperacion(Activo activo, String estrategia, Double prediccion, String robot, Boolean inv) {
		
		long millis = System.currentTimeMillis();
		Orden orden = new Orden();
			orden.setFAlta(millis);
			orden.setFBaja(null);
			orden.setEstrategia(estrategia);
			orden.setTipoActivo(activo);
			orden.setTipoOrden(TipoOrden.CLOSE);
			orden.setRobot(robot);
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
		
		logger.info("SE CALCULA LA PREDICCIÓN -> Robot -> " + bot);		
		Double prediccion = calcularPrediccion(bot);
		logger.info("PREDICCIÓN CALCULADA -> Robot -> " + bot + " Predicción -> " + prediccion);
		
		logger.info("SE CALCULA LA ORDEN -> Robot -> " + bot);		
		Orden orden = calcularOperacion(bot.getActivo(), bot.getEstrategia(), prediccion, bot.getRobot(), bot.getInverso());
		logger.info("ORDEN CALCULADA -> Robot -> " + bot + " -> Orden -> " + orden);
		
		Long fechaHoraMillis = System.currentTimeMillis();
		
		actualizarPrediccionBDs(bot.getActivo(), bot.getEstrategia(), bot.getRobot(), orden.getTipoOrden(), prediccion, fechaHoraMillis);
		logger.info("PREDICCIÓN ACTUALZIDA -> Robot -> " + bot + " Predicciñon -> " + prediccion);
		actualizarUltimaOrden(bot.getActivo(), bot.getEstrategia(), orden, fechaHoraMillis);
		logger.info("ORDEN ANTERIOR ELIMINADA -> Robot -> " + bot);
		guardarNuevaOrden(orden, fechaHoraMillis);
		logger.info("NUEVA ORDEN GUARDADA -> Robot -> " + bot + " -> Orden -> " + orden);

		
	}
	
}
