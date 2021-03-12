package daryl.system.robot.arima.b.predictor.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Orden;
import daryl.system.model.Prediccion;
import daryl.system.model.Robot;
import daryl.system.robot.arima.b.repository.IOrdenRepository;
import daryl.system.robot.arima.b.repository.IPrediccionRepository;

public abstract class ArimaPredictor {


	@Autowired
	protected ConfigData config;
		
	@Autowired
	protected IOrdenRepository ordenRepository;
	@Autowired
	protected IPrediccionRepository prediccionRepository;

	
	//public abstract void calculate(Activo activo, String estrategia);	
	public abstract void calculate(Robot robot);
	protected abstract Double calcularPrediccion(Robot robot);
	//protected abstract Orden calcularOperacion(TipoActivo activo, Estrategia estrategia, Double prediccion);

	//@Async
	protected void actualizarPrediccionBDs(Activo activo, String estrategia, TipoOrden orden, Double prediccionCierre, Long fechaHoraMillis) {
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
				
			prediccionRepository.save(prediccion);
			//////logger.info("Guardamos la prediccion para {} es {}", activo.name(), prediccion);
		} catch (Exception e) {
			//logger.error("No se ha podido guardar la prediccion para el activo: {}", activo.name(), e);
		}
	}
	
	//@Async
	protected void actualizarUltimaOrden(Activo activo, String estrategia, Orden orden, Long fechaHoraMillis) {
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
	
	@Async
	protected void guardarNuevaOrden(Orden orden, Long fechaHoraMillis) {
		try {
			orden.setFAlta(fechaHoraMillis);
			ordenRepository.save(orden);
			////logger.info("Guardamos la orden para {} es {}", orden.getTipoActivo().name(), orden.getTipoOrden());
		}catch (Exception e) {
			//logger.error("No se ha podido guardar la nueva orden para el activo: {}", orden.getTipoActivo().name(), e);
		}
	}
	
	protected final Orden calcularOperacion(Activo activo, String estrategia, Double prediccion, String robot, Boolean inv) {
		
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

	
}
